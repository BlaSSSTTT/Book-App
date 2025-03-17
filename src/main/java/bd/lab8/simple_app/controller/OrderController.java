package bd.lab8.simple_app.controller;

import bd.lab8.simple_app.model.BatchPrint;
import bd.lab8.simple_app.model.Book;
import bd.lab8.simple_app.model.PrintOrder;
import bd.lab8.simple_app.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/orders")
public class OrderController {

    public static final String REDIRECT = "redirect:/orders";

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String showOrdersPage(@RequestParam(value = "page", defaultValue = "1") int page,
                                 @RequestParam(value = "size", defaultValue = "10") int size,
                                 @RequestParam(defaultValue = "number") String sort,
                                 @RequestParam(defaultValue = "asc") String direction,
                                 @RequestParam(required = false) String searchQuery,
                                 Model model) {

        Page<PrintOrder> orderPage = orderService.findAllOrders(page, size, sort, direction, searchQuery);
        model.addAttribute("orders", orderPage);
        model.addAttribute("size", size);
        model.addAttribute("page", size);
        model.addAttribute("currentPage", orderPage.getNumber() + 1);
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("totalItems", orderPage.getTotalElements());
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("searchQuery", searchQuery);
        return "orders";
    }

    @GetMapping("/create")
    public String showCreateOrderForm(Model model) {
        model.addAttribute("newOrder", new PrintOrder());
        model.addAttribute("printTypes", orderService.findAllPrintTypes());
        model.addAttribute("coverTypes", orderService.findAllCoverTypes());
        model.addAttribute("paperTypes", orderService.findAllPaperTypes());
        model.addAttribute("fasteningTypes", orderService.findAllFasteningTypes());
        model.addAttribute("customers", orderService.findAllCustomers());
        model.addAttribute("employees", orderService.findAllEmployees());
        model.addAttribute("orderStatuses", orderService.findAllOrderStatuses());
        return "create_order";
    }

    @PostMapping("/create")
    public String createOrder(@Valid @ModelAttribute("newOrder") PrintOrder newOrder,
                              BindingResult bindingResult,
                              @RequestParam(name = "customer", required = false) Long customerId,
                              @RequestParam(name = "employee", required = false) Long employeeId,
                              @RequestParam(name = "orderStatus", required = false) Long orderStatusId,
                              Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("printTypes", orderService.findAllPrintTypes());
            model.addAttribute("coverTypes", orderService.findAllCoverTypes());
            model.addAttribute("paperTypes", orderService.findAllPaperTypes());
            model.addAttribute("fasteningTypes", orderService.findAllFasteningTypes());
            model.addAttribute("customers", orderService.findAllCustomers());
            model.addAttribute("employees", orderService.findAllEmployees());
            model.addAttribute("orderStatuses", orderService.findAllOrderStatuses());

            return "create_order";
        }

        orderService.createOrder(newOrder, customerId, employeeId, orderStatusId);
        return REDIRECT;
    }

    @GetMapping("/edit/{id}")
    public String showEditOrderForm(@PathVariable("id") Long id, Model model) {
        PrintOrder order = orderService.findOrderById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + id));
        model.addAttribute("updOrder", order);
        model.addAttribute("printTypes", orderService.findAllPrintTypes());
        model.addAttribute("coverTypes", orderService.findAllCoverTypes());
        model.addAttribute("paperTypes", orderService.findAllPaperTypes());
        model.addAttribute("fasteningTypes", orderService.findAllFasteningTypes());
        model.addAttribute("customers", orderService.findAllCustomers());
        model.addAttribute("employees", orderService.findAllEmployees());
        model.addAttribute("orderStatuses", orderService.findAllOrderStatuses());
        return "edit_order";
    }

    @PostMapping("/edit/{id}")
    public String updateOrder(@Valid @ModelAttribute("updOrder") PrintOrder updOrder,
                              BindingResult bindingResult,
                              @PathVariable("id") Long id,
                              @RequestParam Long customer,
                              @RequestParam Long employee,
                              @RequestParam Long orderStatus,
                              Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("printTypes", orderService.findAllPrintTypes());
            model.addAttribute("coverTypes", orderService.findAllCoverTypes());
            model.addAttribute("paperTypes", orderService.findAllPaperTypes());
            model.addAttribute("fasteningTypes", orderService.findAllFasteningTypes());
            model.addAttribute("customers", orderService.findAllCustomers());
            model.addAttribute("employees", orderService.findAllEmployees());
            model.addAttribute("orderStatuses", orderService.findAllOrderStatuses());

            return "edit_order";
        }

        orderService.updateOrder(id, updOrder, customer, employee, orderStatus);
        return REDIRECT;
    }

    @PostMapping("/delete/{id}")
    public String deleteOrder(@PathVariable("id") Long id) {
        orderService.deleteOrder(id);
        return REDIRECT;
    }
}
