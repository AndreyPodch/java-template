package edu.spbu.matrix;

/**
 *
 */
public abstract class Matrix
{
  int h,w;
  abstract public Matrix mul(Matrix o) throws WrongSizeException, MemoryAllocateException;
  abstract public String toString();
  abstract public boolean equals(Object o);
  abstract public double getElement(int i, int j);
}
