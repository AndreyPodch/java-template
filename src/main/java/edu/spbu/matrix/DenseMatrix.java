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
    this.w=0;
    int z;
    while(datascan.hasNextLine())
    {
      z=0;
      this.h++;
      String[] array = datascan.nextLine().split(" ");
      for (String s : array) {
        if(s.length()!=0) {
          double el = Double.parseDouble(s);
          if (!tempdata.add(el)) {
            throw new MemoryAllocateException();
          }
          z++;
        }
      }
      if(this.w==0)
      {
        this.w=z;
      }
      else if(this.w!=z)
      {
        throw new NumberFormatException();
      }
    }
    if ((this.h==0)||(this.w==0))
    {
      throw new NumberFormatException();
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
      if(i!=h-1) alpha.append("\n");
    }
    return alpha.toString();
  }

  @Override
  public Matrix transposition() {
    DenseMatrix m=new DenseMatrix(this.h,this.w);
    for(int i=0;i<this.h;i++)
    {
      for(int j=0;j<this.w;j++)
      {
        m.data[j][i]=this.data[i][j];
      }
    }
    return m;
  }

  @Override public Matrix mul(Matrix o) throws WrongSizeException {
    if (this.w != o.h) {
      throw new WrongSizeException();
    }
    Matrix mulm;
    if (o instanceof DenseMatrix) {
      mulm = this.dtodmul((DenseMatrix) o);
    }
    else
    {
      SparseMatrix s=(SparseMatrix) o;
      DenseMatrix m=this;
      mulm=(s.transposition().mul(m.transposition())).transposition();
    }
    return mulm;
  }

  @Override
  public Matrix mthmul(Matrix o) throws WrongSizeException, InterruptedException {
    if (this.w != o.h) {
      throw new WrongSizeException();
    }
    Matrix mulm;
    if (o instanceof DenseMatrix) {
      mulm = this.mthdtodmul((DenseMatrix) o);
    }
    else
    {
      SparseMatrix s=(SparseMatrix) o;
      DenseMatrix m=this;
      mulm=(s.transposition().mul(m.transposition())).transposition();
    }
    return mulm;
  }

  public DenseMatrix mthdtodmul(final DenseMatrix o) throws InterruptedException {
    final DenseMatrix m=new DenseMatrix(o.w,this.h);
    final DenseMatrix m1=this;
    class ParallelDenseMulMatrix implements Runnable
    {
      final int rs,rf,cs,cf;
      public  ParallelDenseMulMatrix(int rs,int cs, int rf, int cf)
      {
        this.rs=rs;
        this.rf=rf;
        this.cs=cs;
        this.cf=cf;
      }
      @Override
      public void run() {
        for (int i = rs; i < rf; i++) {
          for (int j = cs; j < cf; j++) {
            m.data[i][j] = 0;
            for (int k = 0; k < m1.w; k++) {
              m.data[i][j] += (m1.data[i][k] * o.data[k][j]);
            }
          }
        }
      }
    }
    Thread th1,th2,th3,th4;
    int rmid=m.w/2, cmid=m.h/2;
    th1=new Thread(new ParallelDenseMulMatrix(0,0,rmid,cmid));
    th2=new Thread(new ParallelDenseMulMatrix(rmid,0,m.w,cmid));
    th3=new Thread(new ParallelDenseMulMatrix(0,cmid,rmid,m.h));
    th4=new Thread(new ParallelDenseMulMatrix(rmid,cmid,m.w,m.h));
    th1.start(); th2.start(); th3.start(); th4.start();
    th1.join(); th2.join(); th3.join(); th4.join();
    return m;
  }
  public DenseMatrix dtodmul(DenseMatrix o)
  {
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
  @Override public boolean equals(Object o)
  {
    if (o instanceof DenseMatrix)
    {
      return  this.ddequals(o);
    }
    else
    {
      return o.equals(this);
    }
  }
   public boolean ddequals(Object o) {
     if (getClass() != o.getClass()) return false;
     DenseMatrix m = (DenseMatrix) o;
     if ((this.h != m.h) || (this.w != m.w)) {
       return false;
     }
     for (int i = 0; i < this.h; i++) {
       for (int j = 0; j < this.w; j++) {
         if (this.data[i][j] != m.data[i][j]) {
           return false;
         }
       }
     }
     return true;
   }
}
