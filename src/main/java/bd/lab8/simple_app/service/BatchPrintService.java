package bd.lab8.simple_app.service;

import bd.lab8.simple_app.dto.BatchPrintDto;
import bd.lab8.simple_app.model.*;
import bd.lab8.simple_app.repository.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BatchPrintService {

    private final BatchPrintRepository batchPrintRepository;
    private final OrderRepository orderRepository;
    private final QualityMarkRepository qualityMarkRepository;
    private final OrderStatusRepository orderStatusRepository;

    public BatchPrintService(BatchPrintRepository batchPrintRepository,
                             OrderRepository orderRepository,
                             QualityMarkRepository qualityMarkRepository,
                             OrderStatusRepository orderStatusRepository) {
        this.batchPrintRepository = batchPrintRepository;
        this.orderRepository = orderRepository;
        this.qualityMarkRepository = qualityMarkRepository;
        this.orderStatusRepository = orderStatusRepository;
    }

    @Transactional
    public Page<BatchPrintDto> findBatchPrintResults(int page, int size, String sort, String direction, String searchQuery) {
        size = Math.clamp(size, 1, 100);
        page = Math.max(page - 1, 0);

        Sort sortOrder = direction.equals("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending();
        Pageable pageable = PageRequest.of(page, size, sortOrder);

        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            return batchPrintRepository.findBatchPrintDetails(pageable);
        }

        return batchPrintRepository.searchByNumber(searchQuery.trim(), pageable);
    }

    @Transactional
    public Optional<BatchPrint> findBatchById(Long id) {
        return batchPrintRepository.findById(id);
    }

    @Transactional
    public void createBatch(BatchPrint batchPrint, Long orderId, Long qualityMarkId) {
        Optional<PrintOrder> orderOpt = orderRepository.findById(orderId);
        Optional<QualityMark> qualityMarkOpt = qualityMarkRepository.findById(qualityMarkId);

        if (orderOpt.isPresent() && qualityMarkOpt.isPresent()) {
            PrintOrder order = orderOpt.get();

            batchPrint.setOrder(order);
            batchPrint.setQualityMark(qualityMarkOpt.get());

            batchPrintRepository.save(batchPrint);
        } else {
            throw new IllegalArgumentException("Invalid Order ID, Quality Mark ID");
        }
    }

    @Transactional
    public void updateBatch(Long id, BatchPrint updatedBatchPrint, Long orderId, Long qualityMarkId) {
        Optional<BatchPrint> existingBatchOpt = batchPrintRepository.findById(id);

        if (existingBatchOpt.isPresent()) {
            BatchPrint existingBatch = existingBatchOpt.get();
            updateBatchFields(updatedBatchPrint, existingBatch);

            if (orderRepository.findById(orderId).isPresent()) {
                PrintOrder order = orderRepository.findById(orderId).get();
                existingBatch.setOrder(order);
            }

            if (qualityMarkRepository.findById(qualityMarkId).isPresent()) {
                QualityMark qualityMark = qualityMarkRepository.findById(qualityMarkId).get();
                existingBatch.setQualityMark(qualityMark);
            }

            batchPrintRepository.save(existingBatch);
        } else {
            throw new IllegalArgumentException("Batch Print not found");
        }
    }

    @Transactional
    public void deleteBatch(Long id) {
        batchPrintRepository.deleteById(id);
    }

    public List<PrintOrder> findAllOrders() {
        return orderRepository.findAll();
    }

    public List<QualityMark> findAllQualityMarks() {
        return qualityMarkRepository.findAll();
    }

    public List<OrderStatus> findAllOrderStatuses() {
        return orderStatusRepository.findAll();
    }

        private void updateBatchFields(BatchPrint existingBatch, BatchPrint updatedBatch) {
        updatedBatch.setNumber(existingBatch.getNumber());
        updatedBatch.setBookQuantity(existingBatch.getBookQuantity());
        updatedBatch.setPrintDate(existingBatch.getPrintDate());
    }

    public PrintOrder findOrderById(Long orderId) {
        return orderRepository.findById(orderId).get();
    }

    public QualityMark findQualityMarkById(Long orderId) {
        return qualityMarkRepository.findById(orderId).get();
    }
}
