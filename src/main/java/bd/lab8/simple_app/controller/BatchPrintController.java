package bd.lab8.simple_app.controller;

import bd.lab8.simple_app.dto.BatchPrintDto;
import bd.lab8.simple_app.model.BatchPrint;
import bd.lab8.simple_app.service.BatchPrintService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/batches")
public class BatchPrintController {

    private static final String REDIRECT = "redirect:/batches";

    private final BatchPrintService batchPrintService;

    public BatchPrintController(BatchPrintService batchPrintService) {
        this.batchPrintService = batchPrintService;
    }

    @GetMapping
    public String showBatchesPage(@RequestParam(value = "page", defaultValue = "1") int page,
                                  @RequestParam(value = "size", defaultValue = "10") int size,
                                  @RequestParam(defaultValue = "number") String sort,
                                  @RequestParam(defaultValue = "asc") String direction,
                                  @RequestParam(required = false) String searchQuery,
                                  Model model) {

        Page<BatchPrintDto> batchPage = batchPrintService.findBatchPrintResults(page, size, sort, direction, searchQuery);

        model.addAttribute("batches", batchPage);
        model.addAttribute("size", size);
        model.addAttribute("page", size);
        model.addAttribute("currentPage", batchPage.getNumber() + 1);
        model.addAttribute("totalPages", batchPage.getTotalPages());
        model.addAttribute("totalItems", batchPage.getTotalElements());
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("searchQuery", searchQuery);

        return "batches";
    }

    @GetMapping("/create")
    public String showCreateBatchForm(Model model) {
        model.addAttribute("newBatchPrint", new BatchPrint());
        model.addAttribute("orders", batchPrintService.findAllOrders());
        model.addAttribute("qualityMarks", batchPrintService.findAllQualityMarks());
        model.addAttribute("orderStatuses", batchPrintService.findAllOrderStatuses());
        return "create_batch";
    }

    @PostMapping("/create")
    public String createBatch(@Valid @ModelAttribute("newBatchPrint") BatchPrint newBatchPrint,
                              BindingResult bindingResult,
                              @RequestParam(name = "order", required = false) Long orderId,
                              @RequestParam(name = "qualityMark", required = false) Long qualityMarkId,
                              Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("orders", batchPrintService.findAllOrders());
            model.addAttribute("qualityMarks", batchPrintService.findAllQualityMarks());

            return "create_batch";
        }

        batchPrintService.createBatch(newBatchPrint, orderId, qualityMarkId);
        return REDIRECT;
    }

    @GetMapping("/edit/{id}")
    public String showEditBatchForm(@PathVariable("id") Long id, Model model) {
        BatchPrint batchPrint = batchPrintService.findBatchById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid batch print Id:" + id));
        model.addAttribute("updBatchPrint", batchPrint);
        model.addAttribute("orders", batchPrintService.findAllOrders());
        model.addAttribute("qualityMarks", batchPrintService.findAllQualityMarks());
        model.addAttribute("orderStatuses", batchPrintService.findAllOrderStatuses());
        return "edit_batch";
    }

    @PostMapping("/edit/{id}")
    public String updateBatch(@Valid @ModelAttribute("updBatchPrint") BatchPrint updBatchPrint,
                              BindingResult bindingResult,
                              @PathVariable("id") Long id,
                              @RequestParam("order") Long orderId,
                              @RequestParam("qualityMark") Long qualityMarkId,
                              Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("updBatchPrint", updBatchPrint);
            model.addAttribute("orders", batchPrintService.findAllOrders());
            model.addAttribute("qualityMarks", batchPrintService.findAllQualityMarks());

            return "edit_batch";
        }

        batchPrintService.updateBatch(id, updBatchPrint, orderId, qualityMarkId);
        return REDIRECT;
    }

    @PostMapping("/delete/{id}")
    public String deleteBatch(@PathVariable("id") Long id) {
        batchPrintService.deleteBatch(id);
        return REDIRECT;
    }
}
