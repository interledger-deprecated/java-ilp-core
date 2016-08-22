package org.interledger.ilp.core.events;

import org.interledger.ilp.core.InterledgerPacketHeader;
import org.interledger.ilp.core.Ledger;
import org.interledger.ilp.core.LedgerTransferRejectedReason;

public class LedgerTransferRejectedEvent extends LedgerTransferEvent {

    private static final long serialVersionUID = 5106316912312360715L;

    private LedgerTransferRejectedReason reason;

    public LedgerTransferRejectedEvent(Ledger source,
            InterledgerPacketHeader ilpPacketHeader, String localFromAccount,
            String localToAccount, String localTransferAmount, LedgerTransferRejectedReason reason) {

        super(source, ilpPacketHeader, localFromAccount, localToAccount,
                localTransferAmount);

        this.reason = reason;
    }

    public LedgerTransferRejectedReason getReason() {
        return reason;
    }

}
