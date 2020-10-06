package edu.spbu.matrix;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * Плотная матрица
 */
public class DenseMatrix implements Matrix
{
  ArrayList<Integer> data;
  int w,h;
  /**
   * загружает матрицу из файла
   * @param fileName
   */
  public DenseMatrix(String fileName) {
    Scanner datascan = new Scanner(fileName);
    this.h=0;
    this.data=new ArrayList<>();
    while(datascan.hasNextLine())
    {
      this.h++;
      String[] array = datascan.nextLine().split(" ");
      this.w=array.length;
      for(int i=0;i<array.length;i++)
      {
        int el = Integer.parseInt(array[i]);
        if(!data.add(el))
        {
          System.out.println("Memory allocation error");
        }
      }
    }
  }
  /**
   * однопоточное умнджение матриц
   * должно поддерживаться для всех 4-х вариантов
   *
   * @param o
   * @return
   */
  @Override public Matrix mul(Matrix o)
  {
    return null;
  }

  /**
   * многопоточное умножение матриц
   *
   * @param o
   * @return
   */
  @Override public Matrix dmul(Matrix o)
  {
    return null;
  }

  /**
   * спавнивает с обоими вариантами
   * @param o
   * @return
   */
  @Override public boolean equals(Object o) {
    return false;
  }

}
