package edu.spbu.matrix;

import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;


public class MatrixTest {
  @Test
  public void mulDD() throws Exception{
    Matrix m1 ,m2,mr,m3;

    try {
      m1 = new SparseMatrix("./src/main/m1.txt");
      m2 = new SparseMatrix("./src/main/m2.txt");
      mr = new SparseMatrix("./src/main/mr.txt");
      m3 = m1.mthmul(m2);
      assertEquals(m3, mr);
    } catch (FileNotFoundException e) {
      System.out.println("Sorry, can't find file :(");
      throw new Exception();
    } catch (WrongSizeException e) {
      System.out.println("Matrixes have wrong size :(");
      throw new Exception();
    } catch (NumberFormatException e)
    {
      System.out.println("Matrixes are set incorrectly :(");
      throw new Exception();
    }
  }
}
