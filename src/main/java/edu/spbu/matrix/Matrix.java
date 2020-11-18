package edu.spbu.matrix;

/**
 *
 */
public abstract class Matrix
{
  int h,w;
  abstract public Matrix mul(Matrix o) throws WrongSizeException;
  abstract public Matrix mthmul(Matrix o) throws WrongSizeException, InterruptedException;
  abstract public String toString();
  abstract public Matrix transposition();
  abstract public boolean equals(Object o);
  abstract public double getElement(int i, int j);
}
