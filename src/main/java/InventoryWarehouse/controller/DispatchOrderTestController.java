
package InventoryWarehouse.controller;

import InventoryWarehouse.repository.DispatchOrderRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class DispatchOrderTestController {

    private final DispatchOrderRepository dispatchOrderRepository;

    public DispatchOrderTestController(
            DispatchOrderRepository dispatchOrderRepository) {
        this.dispatchOrderRepository = dispatchOrderRepository;
    }

    @GetMapping("/dispatch-orders-test")
    public List<Map<String, Object>> getDispatchOrders() {

        return dispatchOrderRepository.findAll()
                .stream()
                .map(order -> Map.<String, Object>of(
                        "dispatchOrderId",
                        order.getDispatchOrderId(),

                        "warehouseName",
                        order.getWarehouse().getWarehouseName(),

                        "customerReference",
                        order.getCustomerReference(),

                        "dispatchDate",
                        order.getDispatchDate() == null
                                ? "" : order.getDispatchDate().toString(),

                        "status",
                        order.getStatus().name(),

                        "dispatchedAt",
                        order.getDispatchedAt() == null
                                ? "" : order.getDispatchedAt().toString(),

                        "createdBy",
                        order.getCreatedBy()
                ))
                .toList();
    }
}
