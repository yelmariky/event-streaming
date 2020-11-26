/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.nextdigital.lab.simulator;

import com.github.javafaker.Faker;

import fr.nextdigital.lab.simulator.account.domain.Account;
import fr.nextdigital.lab.simulator.account.domain.AccountStatus;
import fr.nextdigital.lab.simulator.account.service.AccountService;
import fr.nextdigital.lab.simulator.domain.Address;
import fr.nextdigital.lab.simulator.domain.AddressType;
import fr.nextdigital.lab.simulator.inventory.domain.Inventory;
import fr.nextdigital.lab.simulator.inventory.domain.InventoryStatus;
import fr.nextdigital.lab.simulator.order.domain.LineItem;
import fr.nextdigital.lab.simulator.order.domain.Order;
import fr.nextdigital.lab.simulator.warehouse.domain.Warehouse;
import fr.nextdigital.lab.simulator.warehouse.service.WarehouseService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@MessageEndpoint
public class LoadProcessor {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(LoadProcessor.class);
    private final LoadSimulatorProperties properties;
    private final AccountService accountService;
    private final WarehouseService warehouseService;

    public LoadProcessor(LoadSimulatorProperties properties, AccountService accountService,
            WarehouseService warehouseService) {
        this.properties = properties;
        this.accountService = accountService;
        this.warehouseService = warehouseService;
    }

    @ServiceActivator(inputChannel = Processor.INPUT, outputChannel = Processor.OUTPUT)
    public Object process(Message<?> message) {
        return handleRequest(message);
    }

    private String handleRequest(Message<?> message) {
        StringBuffer sb = new StringBuffer();
        switch (properties.getDomain()) {
            case ACCOUNT:
                accountOperation(properties.getOperation(), sb);
                break;
            case ORDER:
                break;
            case WAREHOUSE:
                break;
            case INVENTORY:
                inventoryOperation(properties.getOperation(), sb);
                break;
        }

        return sb.toString();
    }

    private void inventoryOperation(Operation operation, StringBuffer sb) {
        Faker faker = new Faker();

        switch (operation) {
            case CREATE:
                Warehouse warehouse = null;

                try {
                    warehouse = warehouseService.get(1L);
                } catch (Exception ex) {
                	log.error("warehouseService CREATE ",ex);
                }

                if (warehouse == null) {
                    // Create the first warehouse
                    warehouse = warehouseService.create(new Warehouse(new Address(faker.address().streetAddress(),
                            faker.address().buildingNumber(),
                            faker.address().state(),
                            faker.address().city(),
                            faker.address().country(),
                            Integer.parseInt(faker.address().zipCode()))));

                    sb.append("[Warehouse created]\n");
                }

                List<Inventory> inventory = new ArrayList<>();

                LongStream.range(0, properties.getRange())
                        .forEach(a -> inventory.add(new Inventory(InventoryStatus.INVENTORY_CREATED, "SKU-" + a)));

                List<Inventory> results = warehouseService.addInventory(inventory, 1L);

                sb.append(String.format("[%s inventory added to warehouse]\n", results.size()));

                break;
        }
    }

    private void accountOperation(Operation operation, StringBuffer sb) {

        Faker faker = new Faker();

        switch (operation) {
            case CREATE:
                if (properties.getCommand() == Command.POST_ORDER) {
                    // Post new order to the accounts
                    LongStream.range(1, properties.getRange() + 1)
                            .forEach(a -> {
                                try {
                                    Order order = new Order(a, new Address(faker.address().streetAddress(),
                                            faker.address().buildingNumber(),
                                            faker.address().state(),
                                            faker.address().city(),
                                            faker.address().country(),
                                            Integer.parseInt(faker.address().zipCode().substring(0, 4)),
                                            AddressType.SHIPPING));

                                    IntStream.range(0, new Random().nextInt(5))
                                            .forEach(i -> order.getLineItems()
                                                    .add(new LineItem(faker.commerce().productName(),
                                                            "SKU-" + i, new Random().nextInt(3), Double
                                                            .parseDouble(faker.commerce().price(.99, 50.0)), .06)));

                                    Order result = accountService.postOrder(a, order);

                                    sb.append(String.format("[New order posted to account %s]\n", a));
                                } catch (Exception ex) {
                                    log.error("New order posted to account",ex);
                                }
                            });
                } else {
                    LongStream.range(0, properties.getRange())
                            .forEach(a -> accountService
                                    .create(new Account(faker.name().firstName(), faker.name().lastName(), faker
                                            .internet()
                                            .emailAddress(), AccountStatus.ACCOUNT_CREATED)));

                    sb.append(String.format("[%s new accounts created]\n", properties.getRange()));
                }
                break;
        }
    }

}
