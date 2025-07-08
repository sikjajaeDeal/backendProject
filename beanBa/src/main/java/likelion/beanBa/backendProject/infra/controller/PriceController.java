package likelion.beanBa.backendProject.infra.controller;

import likelion.beanBa.backendProject.infra.dto.KamisPriceResponse;
import likelion.beanBa.backendProject.infra.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PriceController {
    private final PriceService priceService;

    @GetMapping("/prices")
    public String showPrices(@RequestParam String itemCode,  Model model) {
        List<KamisPriceResponse.Item> items = priceService.getPriceList(itemCode).block();
        model.addAttribute("items", items);
        return "price";
    }
}
