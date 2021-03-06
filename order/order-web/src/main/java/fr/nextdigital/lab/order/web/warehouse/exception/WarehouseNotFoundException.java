package fr.nextdigital.lab.order.web.warehouse.exception;

/**
 * Exception is thrown when a {@link fr.nextdigital.lab.order.web.warehouse.domain.Warehouse} could not be found with sufficient inventory
 * to fulfill an {@link fr.nextdigital.lab.order.web.domain.Order}.
 *
 * @author Kenny Bastani
 */
public class WarehouseNotFoundException extends RuntimeException {
    public WarehouseNotFoundException() {
    }

    public WarehouseNotFoundException(String message) {
        super(message);
    }

    public WarehouseNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public WarehouseNotFoundException(Throwable cause) {
        super(cause);
    }

    public WarehouseNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean
            writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
