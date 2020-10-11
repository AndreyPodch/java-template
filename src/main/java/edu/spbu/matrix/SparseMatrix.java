package edu.spbu.matrix;

/**
 * Разряженная матрица
 */
public class SparseMatrix extends Matrix {
  /**
   * загружает матрицу из файла
   * @param fileName
   */
  public SparseMatrix(String fileName) {

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
  @Override public String toString() {return "";}
  @Override public double getElement(int i, int j) {return 0;}
  @Override public boolean equals(Object o) {
    return false;
  }
}
