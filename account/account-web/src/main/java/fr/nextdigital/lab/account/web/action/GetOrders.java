package fr.nextdigital.lab.account.web.action;

import fr.nextdigital.lab.account.web.domain.Account;
import fr.nextdigital.lab.domain.Action;
import fr.nextdigital.lab.account.web.order.domain.OrderModule;
import fr.nextdigital.lab.account.web.order.domain.Orders;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Query action to get {@link fr.nextdigital.lab.account.web.order.domain.Order}s for an an {@link Account}
 *
 * @author Kenny Bastani
 */
@Service
@Transactional
public class GetOrders extends Action<Account> {

    private OrderModule orderModule;

    public GetOrders(OrderModule orderModule) {
        this.orderModule = orderModule;
    }

    public Orders apply(Account account) {
        // Get orders from the order service
        return orderModule.getDefaultService()
                .findOrdersByAccountId(account.getIdentity());
    }
}
