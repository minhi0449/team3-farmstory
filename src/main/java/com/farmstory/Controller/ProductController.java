package com.farmstory.Controller;

import com.farmstory.dto.ProductDTO;

import com.farmstory.entity.Product;
import com.farmstory.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

// 수빈님 - product
@Slf4j
@RequiredArgsConstructor
@Controller
public class ProductController {

    private final ProductService productService;
    // 관리자 - 상품 등록
    @GetMapping("/admin/product/register")
    public String productRegister() {
        return "/admin/product/register";
    }
    @PostMapping("/admin/product/register")
    public String productRegister(ProductDTO productDTO, MultipartFile[] images) {

        productService.insertProduct(productDTO,images);
        return "redirect:/admin/product/list";
    }

    // 관리자 - 상품 목록
    @GetMapping("/admin/product/list")
    public String productList(Model model, @PageableDefault(size = 5) Pageable pageable) {
        long count = productService.countAllProducts();
        model.addAttribute("count",count);

        Page<ProductDTO> products =productService.selectProductsForPage(pageable);
        model.addAttribute("products",products.getContent());

        // 현재 페이지 정보와 전체 페이지 수를 전달
        model.addAttribute("currentPage",pageable.getPageNumber());
        model.addAttribute("totalPages",products.getTotalPages());
        return "/admin/product/list";
    }

    // 상품 보기 (상세 페이지)
    @GetMapping("/market/view")
    public String view(@RequestParam("prodNo") int prodNo, Model model) {
        ProductDTO product = productService.findProductById(prodNo);

        model.addAttribute("product", product);

        return "/market/view";
    }



//    @GetMapping({"/market/list", "/market/list/type/{type}"})
//    public String marketlist(Model model, @PathVariable(required = false) String type) {
//        // 상품의 총 갯수 계산
//        long count = productService.countAllProducts();
//        model.addAttribute("count", count);
//
//        if (type == null) {
//            // 전체 상품 조회
//            List<ProductDTO> products = productService.selectProducts();
//            model.addAttribute("products", products);
//        } else {
//            // 특정 타입 상품 조회
//            List<ProductDTO> productsByType = productService.selectsByType(type);
//            model.addAttribute("products", productsByType);
//        }
//
//        return "/market/list";  // 뷰 파일 경로
//    }
@GetMapping({"/market/list", "/market/list/type/{type}"})
public String list(Model model,
                   @PathVariable(required = false) String type,
                   @PageableDefault(size = 5) Pageable pageable) {
    // 상품의 총 갯수 계산
    long count = productService.countAllProducts();
    model.addAttribute("count", count);
    Page<ProductDTO> products;
    if (type == null) {
        // 페이징 처리된 전체 상품 조회
        products = productService.selectProductsForPage(pageable);
        model.addAttribute("products", products.getContent());
    } else {
        // 특정 타입 상품 페이징 처리 조회
        products = productService.selectsByTypeForPage(type, pageable);
        model.addAttribute("products", products.getContent());
    }
    int totalPages = products.getTotalPages();
    int currentPage = pageable.getPageNumber() + 1; // 현재 페이지 (0부터 시작하므로 1을 더함)

    int pageBlockSize = 5; // 한 번에 보여줄 페이지 수
    int startPage = (currentPage - 1) / pageBlockSize * pageBlockSize + 1; // 시작 페이지
    int endPage = Math.min(startPage + pageBlockSize - 1, totalPages); // 끝 페이지

    model.addAttribute("products", products.getContent());
    model.addAttribute("currentPage", currentPage);
    model.addAttribute("totalPages", totalPages);
    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);
    model.addAttribute("hasPrev", startPage > 1);
    model.addAttribute("hasNext", endPage < totalPages);
    return "/market/list";  // 뷰 파일 경로
}

    @PostMapping("/market/delete")
    public String delete(@RequestParam("prodNo") List<String> products) {

        for (String prodNo  : products) {
            productService.deleteProductById(Integer.parseInt(prodNo));
        }
        return "redirect:/admin/product/list";  // 삭제 후 상품 목록으로 리다이렉트
    }

    // 상품 수정
    @GetMapping("/admin/product/modify")
    public String modifyselect(@RequestParam("prodNo") int prodNo, Model model) {

        ProductDTO product = productService.findProductById(prodNo);

        model.addAttribute("product", product);

        return "/admin/product/modify";
    }
    @PostMapping("/admin/product/modify")
    public String modify(@RequestParam("prodNo") int prodNo, ProductDTO productDTO, MultipartFile[] images) {

        productService.updateProduct(productDTO, images);

        return "redirect:/admin/product/list";
    }
}


