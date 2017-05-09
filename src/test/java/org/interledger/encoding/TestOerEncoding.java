package org.interledger.encoding;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

public class TestOerEncoding {

  @Test
  public void draftTest() {

    byte[] buffer = null;

    /* read and write some bytes */
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OerOutputStream os = new OerOutputStream(baos)) {

      os.write16BitUInt(0);

      byte[] bytes = new byte[10];
      java.util.Arrays.fill(bytes, (byte) 0);
      os.writeOctetString(bytes);

      buffer = baos.toByteArray();
    } catch (IOException e) {
      fail("failed " + e.toString());
    }

    try (ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
        OerInputStream is = new OerInputStream(bais)) {

      assertEquals(0, is.read16BitUInt());

      byte[] bytes = is.readOctetString();
      for (int idx = 0; idx < bytes.length; idx++) {
        assertEquals(0, bytes[idx]);
      }
    } catch (IOException e) {
      fail("failed " + e.toString());
    }
  }

  @Test
  public void test_8BitUInt() throws IOException {

    byte[] buffer = null;

    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OerOutputStream os = new OerOutputStream(baos)) {

      os.write8BitUInt(0);
      os.write8BitUInt(127);
      os.write8BitUInt(255);

      buffer = baos.toByteArray();
    }

    try (ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
        OerInputStream is = new OerInputStream(bais)) {

      assertEquals(0, is.read8BitUInt());
      assertEquals(127, is.read8BitUInt());
      assertEquals(255, is.read8BitUInt());
    }
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void test_8BitUInt_Range() throws IOException {
    try(OerOutputStream os = new OerOutputStream(null)) {
      os.write8BitUInt(255 + 1);
    }
  }

  @Test
  public void test_16BitUInt() throws IOException {

    byte[] buffer = null;

    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OerOutputStream os = new OerOutputStream(baos)) {

      os.write16BitUInt(0);
      os.write16BitUInt(255);
      os.write16BitUInt(32767);
      os.write16BitUInt(65535);

      buffer = baos.toByteArray();
    }

    try (ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
        OerInputStream is = new OerInputStream(bais)) {

      assertEquals(0, is.read16BitUInt());
      assertEquals(255, is.read16BitUInt());
      assertEquals(32767, is.read16BitUInt());
      assertEquals(65535, is.read16BitUInt());
    }
  }

  @Test(expected=IllegalArgumentException.class)
  public void test_16BitUInt_Range() throws IOException {
    try(OerOutputStream os = new OerOutputStream(null)) {
      os.write16BitUInt(65535 + 1);
    }
  }

  @Test
  public void test_32BitUInt() throws IOException {

    byte[] buffer = null;

    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OerOutputStream os = new OerOutputStream(baos)) {

      os.write32BitUInt(0);
      os.write32BitUInt(255);
      os.write32BitUInt(32767);
      os.write32BitUInt(65535);
      os.write32BitUInt(Integer.MAX_VALUE);
      os.write32BitUInt(4294967295L);

      buffer = baos.toByteArray();
    }

    try (ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
        OerInputStream is = new OerInputStream(bais)) {

      assertEquals(0, is.read32BitUInt());
      assertEquals(255, is.read32BitUInt());
      assertEquals(32767, is.read32BitUInt());
      assertEquals(65535, is.read32BitUInt());
      assertEquals(Integer.MAX_VALUE, is.read32BitUInt());
      assertEquals(4294967295L, is.read32BitUInt());
    }
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void test_32BitUInt_Range() throws IOException {
    try(OerOutputStream os = new OerOutputStream(null)) {
      os.write32BitUInt(4294967295L + 1);
    }
  }
  
  @Test
  public void test_VarInt() throws IOException {
    byte[] buffer = null;

    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OerOutputStream os = new OerOutputStream(baos)) {

      os.writeVarUInt(0);
      os.writeVarUInt(255);
      os.writeVarUInt(32767);
      os.writeVarUInt(65535);
      os.writeVarUInt(65536);
      os.writeVarUInt(16777215);

      buffer = baos.toByteArray();
    }

    try (ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
        OerInputStream is = new OerInputStream(bais)) {

      assertEquals(0, is.readVarUInt());
      assertEquals(255, is.readVarUInt());
      assertEquals(32767, is.readVarUInt());
      assertEquals(65535, is.readVarUInt());
      assertEquals(65536, is.readVarUInt());
      assertEquals(16777215, is.readVarUInt());
    }
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void test_VarInt_Range() throws IOException {
    try(OerOutputStream os = new OerOutputStream(null)) {
      os.writeVarUInt(16777215 + 1);
    }
  }

  @Test
  public void test_OctetString() throws IOException {
    byte[] buffer = null;

    byte[] octetString = "Test octet-string content 1234567890ABCDEF".getBytes();
    
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OerOutputStream os = new OerOutputStream(baos)) {

      os.writeOctetString(octetString);

      buffer = baos.toByteArray();
    }

    try (ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
        OerInputStream is = new OerInputStream(bais)) {
      
      assertArrayEquals(octetString, is.readOctetString());
    }
  }
  
  
  @Test(expected=IllegalArgumentException.class)
  public void test_OctetString_Null() throws IOException {
    try(OerOutputStream os = new OerOutputStream(null)) {
      os.writeOctetString(null);
    }
  }
  
}
