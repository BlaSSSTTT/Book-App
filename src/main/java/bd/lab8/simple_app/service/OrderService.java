package bd.lab8.simple_app.service;

import bd.lab8.simple_app.model.*;
import bd.lab8.simple_app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final BatchPrintRepository batchPrintRepository;
    private final PrintTypeRepository printTypeRepository;
    private final PaperTypeRepository paperTypeRepository;
    private final CoverTypeRepository coverTypeRepository;
    private final FasteningTypeRepository fasteningTypeRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final OrderBookRepository orderBookRepository;


    @Autowired
    public OrderService(OrderRepository orderRepository, BatchPrintRepository batchPrintRepository,
                        PrintTypeRepository printTypeRepository, PaperTypeRepository paperTypeRepository,
                        CoverTypeRepository coverTypeRepository, FasteningTypeRepository fasteningTypeRepository,
                        CustomerRepository customerRepository, EmployeeRepository employeeRepository,
                        OrderStatusRepository orderStatusRepository, OrderBookRepository orderBookRepository) {
        this.orderRepository = orderRepository;
        this.batchPrintRepository = batchPrintRepository;
        this.printTypeRepository = printTypeRepository;
        this.paperTypeRepository = paperTypeRepository;
        this.coverTypeRepository = coverTypeRepository;
        this.fasteningTypeRepository = fasteningTypeRepository;
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.orderBookRepository = orderBookRepository;
    }

    @Transactional
    public Page<PrintOrder> findAllOrders(int page, int size, String sort, String direction, String searchQuery) {
        size = Math.clamp(size, 1, 100);
        page = Math.max(page - 1, 0);

        Sort sortOrder = direction.equals("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending();
        Pageable pageable = PageRequest.of(page, size, sortOrder);

        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            return orderRepository.findAll(pageable);
        }

        return orderRepository.findByNumberContainingIgnoreCase(searchQuery.trim(), pageable);
    }

    @Transactional
    public Optional<PrintOrder> findOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Transactional
    public void createOrder(PrintOrder orderToCreate, Long customerId, Long employeeId, Long orderStatusId) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        Optional<Employee> employeeOpt = employeeRepository.findById(employeeId);
        Optional<OrderStatus> orderStatusOpt = orderStatusRepository.findById(orderStatusId);

        if (customerOpt.isPresent() && employeeOpt.isPresent() && orderStatusOpt.isPresent()) {
            orderToCreate.setCustomer(customerOpt.get());
            orderToCreate.setEmployee(employeeOpt.get());
            orderToCreate.setOrderStatus(orderStatusOpt.get());

            if (orderToCreate.getPrice() == null) {
                orderToCreate.setPrice(0.0);
            }

            orderRepository.save(orderToCreate);
        } else {
            throw new IllegalArgumentException("Invalid Customer ID, Employee ID, or Order Status ID");
        }
    }

    @Transactional
    public void updateOrder(Long id, PrintOrder updatedOrder, Long customerId, Long employeeId, Long orderStatusId) {
        Optional<PrintOrder> existingOrderOpt = orderRepository.findById(id);
        if (existingOrderOpt.isPresent()) {
            PrintOrder existingOrder = existingOrderOpt.get();
            updateOrderFields(existingOrder, updatedOrder);

            customerRepository.findById(customerId).ifPresent(existingOrder::setCustomer);
            employeeRepository.findById(employeeId).ifPresent(existingOrder::setEmployee);
            orderStatusRepository.findById(orderStatusId).ifPresent(existingOrder::setOrderStatus);

            orderRepository.save(existingOrder);
        }
    }

    @Transactional
    public void deleteOrder(Long id) {
        orderBookRepository.deleteByOrderId(id);
        batchPrintRepository.deleteByOrderId(id);
        orderRepository.deleteById(id);
    }

    public List<PrintType> findAllPrintTypes() {
        return printTypeRepository.findAll();
    }

    public List<CoverType> findAllCoverTypes() {
        return coverTypeRepository.findAll();
    }

    public List<PaperType> findAllPaperTypes() {
        return paperTypeRepository.findAll();
    }

    public List<FasteningType> findAllFasteningTypes() {
        return fasteningTypeRepository.findAll();
    }

    public List<Customer> findAllCustomers() {
        return customerRepository.findAll();
    }

    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    public List<OrderStatus> findAllOrderStatuses() {
        return orderStatusRepository.findAll();
    }

    private void updateOrderFields(PrintOrder existingOrder, PrintOrder updatedOrder) {
        existingOrder.setNumber(updatedOrder.getNumber());
        existingOrder.setPrintType(updatedOrder.getPrintType());
        existingOrder.setPaperType(updatedOrder.getPaperType());
        existingOrder.setCoverType(updatedOrder.getCoverType());
        existingOrder.setFasteningType(updatedOrder.getFasteningType());
        existingOrder.setIsLaminated(updatedOrder.getIsLaminated());
        existingOrder.setOrderStatus(updatedOrder.getOrderStatus());
        existingOrder.setRegistrationDate(updatedOrder.getRegistrationDate());
        existingOrder.setCompletionDate(updatedOrder.getCompletionDate());
    }
}
