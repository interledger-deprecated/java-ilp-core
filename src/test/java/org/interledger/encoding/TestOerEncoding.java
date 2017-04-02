package org.interledger.encoding;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

public class TestOerEncoding {

    @Test
    public void draftTest() /*read and write some bytes */{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OerOutputStream os = new OerOutputStream(baos);
        try {
            os.write16BitUInt(0);
            byte[] bytes = new byte[10];
            java.util.Arrays.fill( bytes, (byte) 0 );
            os.writeOctetString(bytes);
            // assertEquals(12 ,baos.size());
        } catch (IOException e) {
            fail("failed "+e.toString());
        } finally {
            os.close();
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        OerInputStream is = new OerInputStream(bais);
        try{
            assertEquals(0, is.read16BitUInt());
            byte[] bytes = is.readOctetString();
            for (int idx=0; idx<bytes.length; idx++){
                System.out.println(idx);
                assertEquals(0, bytes[idx]);
            }
        }catch(IOException e){
            fail("failed "+e.toString());
        } finally {
            is.close(); 
        }


    }

}
