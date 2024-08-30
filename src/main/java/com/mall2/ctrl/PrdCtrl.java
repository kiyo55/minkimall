package com.mall2.ctrl;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.mall.common.MaskingUtil;
import com.mall.prd.FileStorageService;
import com.mall.prd.PrdSvc;
import com.mall.prd.PrdVo;
import com.mall.review.ReviewSvc;
import com.mall.review.ReviewVo;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;


@RequestMapping("/prd/")
@Controller
public class PrdCtrl {
	
	@Autowired
	PrdSvc prdSvc;
	
	@Autowired
	ReviewSvc reviewSvc;	
	
	@Autowired
    FileStorageService fileStorageService;
		
	@Autowired
	HttpSession session;
	
	@GetMapping("prdList") //상품 리스트 관리
	public String prdList(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
			Model model) {
		System.out.println("prdList");
		
		int pageSize = 10;
		
		List<PrdVo> li=prdSvc.prdList(pageSize, pageNumber);		
		model.addAttribute("li", li);
		model.addAttribute("lisize", li.size()); //현재 보고있는 페이지의 건수
		
		model.addAttribute("pageSize", pageSize); //페이지당 건수
		model.addAttribute("currentPage", pageNumber); //현재 페이지 번호

		int totalRecords = prdSvc.totalCount(); //전체 건수
		int totalPages = (int) Math.ceil(totalRecords * 1.0 / pageSize); //전체 페이지 개수
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("totalRecords", totalRecords);
		
		//카테고리별 제품 건수 및 비중
		List<PrdVo> cntLi=prdSvc.countByCtgr();
		model.addAttribute("cntLi", cntLi);
		
		return "prd/prdList";
	}
	
	@GetMapping("prdList4user") //상품 전체보기
	public String prdList4user(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
			Model model) {
	    System.out.println("prdList4user");
	    
	    int pageSize = 8;
	    
	    List<PrdVo> li = prdSvc.prdList(pageSize, pageNumber);
	    
	    for (PrdVo m : li) {
	        m.checkIfNew();
	    }
	    
	    model.addAttribute("li", li);	    
	    model.addAttribute("pageSize", pageSize);
		model.addAttribute("currentPage", pageNumber);
		
		int totalRecords = prdSvc.totalCount();
		int totalPages = (int) Math.ceil(totalRecords * 1.0 / pageSize);
		model.addAttribute("totalPages", totalPages);

	    //제품별 후기 건수 가져오기
	    List<ReviewVo> reviewCountList = reviewSvc.getReviewCountList();
	    
	    // product_code를 기준으로 후기 건수를 매칭하기 위해 Map으로 변환
	    Map<String, Integer> reviewCountMap = reviewCountList.stream()
	            .collect(Collectors.toMap(ReviewVo::getProduct_code, ReviewVo::getReview_count));
	    
	    // 각 상품에 후기 건수 추가
	    li.forEach(m -> {
	        int reviewCount= reviewCountMap.getOrDefault(m.getProduct_code(), 0);
	        m.setReviewCount(reviewCount); // PrdVo의 reviewCount에 값 전달
	    });
	    
	    //카테고리 리스트
	    List<PrdVo> ctgrList = prdSvc.ctgrList();
	    model.addAttribute("ctgrList", ctgrList);	    
	    
	    return "prd/prdList4user";
	}
	
	@GetMapping("prdListByCtgr") //category별 모아보기
	public String prdListByCtgr(Model model, PrdVo vo) {
		System.out.println("prdList");
		
		List<PrdVo> li=prdSvc.prdListByCtgr(vo);		
		model.addAttribute("li", prdSvc.prdListByCtgr(vo));
		model.addAttribute("lisize", li.size());
		
		if (!li.isEmpty()) {
			model.addAttribute("ctgr", li.get(0).getCategory()); //첫째 결과의 category 대표로 출력
		}
		
		//카테고리 리스트
	    List<PrdVo> ctgrList = prdSvc.ctgrList();
	    model.addAttribute("ctgrList", ctgrList);
		
		return "prd/prdListByCtgr";
	}	
	
	@PostMapping("seachPrd") //상품 검색
	public String seachPrd(@RequestParam (value = "searchCdt", defaultValue = "pname", required = false) String searchCdt,
			@RequestParam (value = "searchKw", defaultValue = "", required = false) String searchKw,
			Model model) {
		System.out.println("seachPrd");		
				
		List<PrdVo> li=prdSvc.seachPrd(searchCdt, searchKw);
		model.addAttribute("li", li);
		model.addAttribute("lisize", li.size());
		
		System.out.println("조건: " + searchCdt + " / 검색어: " + searchKw + " / 건수: "+ li.size());
		
		return "prd/seachPrdResult";
	}
	
	@GetMapping("viewPrd") //상품 상세보기
	public String viewPrd(Model model, PrdVo vo, ReviewVo rvo) {
		System.out.println("viewPrd code: " + vo.getProduct_code());
		
		//사용자 로그인 여부에 따라 구매 버튼 출력 결정
		String uid=(String) session.getAttribute("username");
		vo.setUid(uid);
		
		// 모델에 제품 정보 추가
		PrdVo product = prdSvc.viewPrd(vo);
	    model.addAttribute("product", product);
	    
	    // 제품별 후기 조회
	    List<ReviewVo> reviewList = reviewSvc.reviewList(rvo);

	    // 각 후기 작성자 이름 마스킹 처리
	    reviewList.forEach(rv -> {
	    	rv.setUname(MaskingUtil.maskName(rv.getUname()));
	    });

	    model.addAttribute("reviewList", reviewList); // 후기 리스트
	    model.addAttribute("revListSize", reviewList.size()); // 후기 건수
	    
	    // 동일 category의 제품 추천받기. 상세보는 건 제외
	    List<PrdVo> rcmList = prdSvc.rcmPrdList(product.getCategory(), product.getProduct_code());
	    model.addAttribute("rcmList", rcmList);	    

		return "prd/viewPrd";
	}
	
	@GetMapping("ordQntByPrd") //제품별 주문량
	public String ordQntByPrd(Model model) {
		System.out.println("ordQntByPrd");
				
		model.addAttribute("li", prdSvc.ordQntByPrd());
		
		int totalOrd = prdSvc.totalOrd();
		model.addAttribute("totalOrd", totalOrd);
		
		return "prd/ordQntByPrd";
	}
	
	@GetMapping("salesByPrd")
	public String salesByPrd(Model model) {
		System.out.println("salesByPrd");
		
		model.addAttribute("li", prdSvc.salesByPrd()); //제품별 매출		
		int totalSales = prdSvc.totalSales(); //총매출
		
		// PrdVo 객체에 총매출 설정 및 포맷팅
	    PrdVo vo = new PrdVo();
	    vo.setTotalSales(totalSales);
	    String fmtTotalSales = vo.getFmtTotalSales();
	    
	    // 포맷된 총매출 값을 모델에 추가
	    model.addAttribute("fmtTotalSales", fmtTotalSales);
		
		return "prd/salesByPrd";
	}
	
	//----------------------------------------------
	@GetMapping("addPrdForm")
	public String addPrdForm() {
		System.out.println("addPrdForm");
		
		return "prd/addPrdForm";
	}
	
	@PostMapping("addPrd")
	public String addPrd(MultipartHttpServletRequest request) throws Exception {
	    System.out.println("addPrd");

	    PrdVo vo = new PrdVo();

	    // 첨부파일 처리
	    Part filePart = request.getPart("pimg");
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        
        //FileStorageService에서 정의한 경로(resources/static/img)로 저장하도록 설정
        String realFolder = fileStorageService.getUploadDir();
        
        //파일 경로 생성        
	    File file = new File(realFolder, fileName);
        filePart.write(file.getAbsolutePath());

        // 콘솔에 저장된 파일 경로 출력
        System.out.println("저장 경로: " + file.getAbsolutePath());

	    // 기타 값 처리
	    vo.setPimgStr(fileName);
	    vo.setPname(request.getParameter("pname"));	    
	    vo.setPdesc(request.getParameter("pdesc"));
	    vo.setPdetail(request.getParameter("pdetail"));	    
	    vo.setPprice(Integer.parseInt(request.getParameter("pprice")));
	    vo.setPstock(Integer.parseInt(request.getParameter("pstock")));
	    vo.setPdelifee(Integer.parseInt(request.getParameter("pdelifee")));
	    vo.setPquantity(1);
	    
	    // 카테고리 처리
	    String category = request.getParameter("category");
	    String customCategory = request.getParameter("categoryInput");
	    
	    // customCategory가 있으면 해당 값을 category에 할당
	    if (customCategory != null && !customCategory.isEmpty()) {
	        category = customCategory;
	    }
	    
	    // 최종적으로 category 필드에 값 설정
	    vo.setCategory(category);
	    
	    // db에 추가
	    prdSvc.addPrd(vo);
	    
	    //System.out.println("pdetail received: " + request.getParameter("pdetail"));

	    return "redirect:prdList";
	}
	
}
