package org.interledger.ilp.core.events;

import org.interledger.ilp.core.InterledgerPacketHeader;
import org.interledger.ilp.core.Ledger;

public class LedgerTransferPreparedEvent extends LedgerTransferEvent {

    private static final long serialVersionUID = 4965377395551079045L;

    public LedgerTransferPreparedEvent(Ledger source,
            InterledgerPacketHeader ilpPacketHeader, String localFromAccount,
            String localToAccount, String localTransferAmount) {

        super(source, ilpPacketHeader, localFromAccount, localToAccount,
                localTransferAmount);
    }

}
