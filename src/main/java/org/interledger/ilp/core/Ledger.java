package org.interledger.ilp.core;

import org.interledger.cryptoconditions.Fulfillment;
import org.interledger.ilp.core.events.LedgerEventHandler;

public interface Ledger {

    /**
     * Retrieve some meta-data about the ledger.
     *
     * @return <code>LedgerInfo</code>
     */
    LedgerInfo getInfo();

    /**
     * Initiates a ledger-local transfer.
     *
     * @param transfer <code>LedgerTransfer</code>
     */
    void send(LedgerTransfer transfer);

    /**
     * Reject a transfer
     *
     * This should only be allowed if the entity rejecting the transfer is the
     * receiver
     *
     * @param transfer
     * @param reason
     */
    void rejectTransfer(LedgerTransfer transfer, LedgerTransferRejectedReason reason);

    /**
     * Submit a fulfillment to a ledger.
     *
     * The ledger will execute all transfers that are fulfilled by this
     * fulfillment.
     *
     * @param fulfillment the fulfillment for this transfer
     */
    void fulfillCondition(Fulfillment fulfillment);

    /**
     * Register an event handler for one of the LedgerEvents
     *
     * @param handler
     */
    void registerEventHandler(LedgerEventHandler<?> handler);

}
