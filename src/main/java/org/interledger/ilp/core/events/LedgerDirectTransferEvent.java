package org.interledger.ilp.core.events;

import org.interledger.ilp.core.InterledgerPacketHeader;
import org.interledger.ilp.core.Ledger;

public class LedgerDirectTransferEvent extends LedgerTransferEvent {

    private static final long serialVersionUID = 6955247421334987499L;

    public LedgerDirectTransferEvent(Ledger source,
            InterledgerPacketHeader ilpPacketHeader, String localFromAccount,
            String localToAccount, String localTransferAmount) {

        super(source, ilpPacketHeader, localFromAccount, localToAccount,
                localTransferAmount);

        //For a direct transfer the header should be for an optimistic mode ILP payment
        if (!ilpPacketHeader.isOptimisticModeHeader()) {
            throw new IllegalArgumentException("A direct transfer should not have a condition or timeout in the ILP header.");
        }

    }

}
