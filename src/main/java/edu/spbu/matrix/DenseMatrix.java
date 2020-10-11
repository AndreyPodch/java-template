package edu.spbu.matrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class DenseMatrix extends Matrix
{
  double[][] data;
  public DenseMatrix(String fileName) throws FileNotFoundException,MemoryAllocateException,NumberFormatException {
    Scanner datascan;
    datascan = new Scanner(new File(fileName));
    ArrayList<Double> tempdata=new ArrayList<>();
    this.h=0;
    while(datascan.hasNextLine())
    {
      this.h++;
      String[] array = datascan.nextLine().split(" ");
      this.w=array.length;
      for (String s : array) {
        double el = Double.parseDouble(s);
        if (!tempdata.add(el)) {
          throw new MemoryAllocateException("Err");
        }
      }
    }
    this.data=new double[h][w];
    for (int i=0;i<tempdata.size();i++)
    {
      this.data[i/w][i%w]=tempdata.get(i);
    }
  }
  @Override
  public double getElement(int i, int j)
  {
    return data[i][j];
  }
  public DenseMatrix(int w,int h)
  {
    this.w=w;
    this.h=h;
    this.data=new double[h][w];
  }
  @Override
  public String toString() {
    StringBuilder alpha= new StringBuilder();
    for(int i=0;i<h;i++)
    {
      for(int j=0;j<w;j++)
      {
        alpha.append(data[i][j]).append(" ");
      }
      alpha.append("\n");
    }
    return alpha.toString();
  }

  @Override public Matrix mul(Matrix o) throws WrongSizeException
  {
    if(this.w!=o.h)
    {
      throw new WrongSizeException("Err");
    }
    DenseMatrix m=new DenseMatrix(o.w,this.h);
    for(int i=0;i<m.h;i++)
    {
      for(int j=0;j<m.w;j++)
      {
        m.data[i][j]=0;
        for(int k=0;k<o.h;k++)
        {
          m.data[i][j]+=this.data[i][k]*(o.getElement(k,j));
        }
      }
    }
    return m;
  }
  @Override public boolean equals(Object o) {
    if (getClass() != o.getClass()) return false;
    Matrix m=(Matrix) o;
    if((this.h!=m.h)||(this.w!=m.w))
    {
      return false;
    }
    for(int i=0;i<this.h;i++)
    {
      for(int j=0;j<this.w;j++)
      {
        if (this.data[i][j]!=m.getElement(i,j))
        {
          return false;
        }
      }
    }
    return true;
  }

}
