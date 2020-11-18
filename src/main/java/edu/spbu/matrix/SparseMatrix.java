package edu.spbu.matrix;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Разряженная матрица
 */
public class SparseMatrix extends Matrix {
  /**
   * загружает матрицу из файла
   *
   */
  double[] elements;
  int[] columnIndex;
  int[] NumElInRow;

  public SparseMatrix(int h, int w) {
    this.h = h;
    this.w = w;
  }

  public SparseMatrix(String fileName) throws FileNotFoundException, NumberFormatException {
    Scanner datascan;
    datascan = new Scanner(new File(fileName));
    ArrayList<Double> tempdata = new ArrayList<>();
    ArrayList<Integer> numColumn = new ArrayList<>();
    ArrayList<Integer> ElementsInColumn = new ArrayList<>();
    ElementsInColumn.add(0);
    this.h = 0;
    this.w = 0;
    int z, kol, numst = -1;
    while (datascan.hasNextLine()) {
      numst++;
      z = 0;
      kol = 0;
      this.h++;
      String[] array = datascan.nextLine().split(" ");
      for (String s : array) {
        if (s.length() != 0) {
          double el = Double.parseDouble(s);
          if (el != 0) {
            kol++;
            tempdata.add(el);
            numColumn.add(z);
          }
          z++;
        }
      }
      if (this.w == 0) {
        this.w = z;
      } else if (this.w != z) {
        throw new NumberFormatException();
      }
      ElementsInColumn.add(kol + ElementsInColumn.get(numst));
    }
    this.elements = new double[tempdata.size()];
    this.columnIndex = new int[numColumn.size()];
    for (int i = 0; i < tempdata.size(); i++) {
      this.elements[i] = tempdata.get(i);
      this.columnIndex[i] = numColumn.get(i);
    }
    this.NumElInRow = new int[ElementsInColumn.size()];
    for (int i = 0; i < ElementsInColumn.size(); i++) {
      this.NumElInRow[i] = ElementsInColumn.get(i);
    }
  }

  @Override
  public Matrix transposition() {
    SparseMatrix st = new SparseMatrix(this.w, this.h);
    ArrayList<TreeMap<Integer, Double>> tempdata = new ArrayList<>();
    for (int i = 0; i < st.h; i++) {
      TreeMap<Integer, Double> add1 = new TreeMap<>();
      tempdata.add(add1);
    }
    int nextrow = 1;
    for (int i = 0; i < this.columnIndex.length; i++) {
      if (i >= this.NumElInRow[nextrow]) nextrow++;
      (tempdata.get(this.columnIndex[i])).put(nextrow - 1, elements[i]);
    }
    st.NumElInRow = new int[st.h + 1];
    st.NumElInRow[0] = 0;
    int siz = 0;
    for (int i = 0; i < st.h; i++) {
      siz += tempdata.get(i).size();
      st.NumElInRow[i + 1] = siz;
    }
    st.elements = new double[this.elements.length];
    st.columnIndex = new int[this.elements.length];
    int obg = 0;
    for (int i = 0; i < st.h; i++) {
      for (Map.Entry<Integer, Double> entry : tempdata.get(i).entrySet()) {
        st.columnIndex[obg] = entry.getKey();
        st.elements[obg] = entry.getValue();
        obg++;
      }
    }
    return st;
  }

  /**
   * однопоточное умнджение матриц
   * должно поддерживаться для всех 4-х вариантов
   *
   */
  @Override
  public Matrix mul(Matrix o) throws WrongSizeException {
    if (this.w != o.h) {
      throw new WrongSizeException();
    }
    Matrix mulm;
    if (o instanceof DenseMatrix) mulm = this.stodmul(o);
    else
    {
      mulm=this.stosmul(o);
    }
    return mulm;
  }

  @Override
  public Matrix mthmul(Matrix o) throws WrongSizeException, InterruptedException {
    if (this.w != o.h) {
      throw new WrongSizeException();
    }
    Matrix mulm;
    if (o instanceof DenseMatrix) mulm = this.stodmul(o);
    else
    {
      mulm=this.mthstosmul(o);
    }
    return mulm;
  }
  public SparseMatrix mthstosmul(final Matrix o) throws InterruptedException {
    final SparseMatrix s2=(SparseMatrix) o.transposition();
    final SparseMatrix s1=this;
    final SparseMatrix res=new SparseMatrix(s1.h,s2.h);
    final ArrayList<Double> tempdata = new ArrayList<>();
    ArrayList<Double> data1 = new ArrayList<>();
    ArrayList<Double> data2 = new ArrayList<>();
    ArrayList<Double> data3 = new ArrayList<>();
    ArrayList<Double> data4 = new ArrayList<>();
    ArrayList<Integer> Column1 = new ArrayList<>();
    ArrayList<Integer> Column2 = new ArrayList<>();
    ArrayList<Integer> Column3 = new ArrayList<>();
    ArrayList<Integer> Column4 = new ArrayList<>();
    final ArrayList<Integer> numColumn = new ArrayList<>();
    res.NumElInRow = new int[s2.h+1];
    res.NumElInRow[0]=0;
    class ParallelSparseMulMatrix implements Runnable
    {
      final ArrayList<Double> data;
      final ArrayList<Integer> Column;
      final int sr,fr;
      public  ParallelSparseMulMatrix(ArrayList<Double> data,ArrayList<Integer> Column, int sr, int fr)
      {
        this.data=data;
        this.Column=Column;
        this.sr=sr;
        this.fr=fr;
      }
      @Override
      public void run() {
        for (int i = sr; i < fr; i++) {
          res.NumElInRow[i+1]=res.NumElInRow[i];
          int st = s1.NumElInRow[i], fn = s1.NumElInRow[i + 1];
          double tempel;
          int it1, it2;
          for (int j = 0; j < s2.NumElInRow.length - 1; j++) {
            int sts = s2.NumElInRow[j], fns = s2.NumElInRow[j + 1];
            tempel = 0;
            it1 = st;
            it2 = sts;
            while ((it1 < fn) && (it2 < fns)) {
              if (s1.columnIndex[it1] == s2.columnIndex[it2]) {
                tempel += s1.elements[it1] * s2.elements[it2];
                it1++;
                it2++;
              } else if (s1.columnIndex[it1] < s2.columnIndex[it2]) {
                it1++;
              } else {
                it2++;
              }
            }
            if (tempel != 0) {
              res.NumElInRow[i+1]++;
              data.add(tempel);
              Column.add(j);
            }
          }
        }
      }
    }
    int f1,f2,f3,f4;
    f4=s1.NumElInRow.length-1;
    f1=f4/4;
    f2=f4/2;
    f3=f2+f1;
    Thread th1,th2,th3,th4;
    th1=new Thread(new ParallelSparseMulMatrix(data1,Column1,0,f1));
    th2=new Thread(new ParallelSparseMulMatrix(data2,Column2,f1,f2));
    th3=new Thread(new ParallelSparseMulMatrix(data3,Column3,f2,f3));
    th4=new Thread(new ParallelSparseMulMatrix(data4,Column4,f3,f4));
    th1.start(); th2.start(); th3.start(); th4.start();
    th1.join(); th2.join(); th3.join(); th4.join();
    tempdata.addAll(data1);
    tempdata.addAll(data2);
    tempdata.addAll(data3);
    tempdata.addAll(data4);
    numColumn.addAll(Column1);
    numColumn.addAll(Column2);
    numColumn.addAll(Column3);
    numColumn.addAll(Column4);
    res.elements = new double[tempdata.size()];
    res.columnIndex = new int[numColumn.size()];
    for (int i = 0; i < tempdata.size(); i++) {
      res.elements[i] = tempdata.get(i);
      res.columnIndex[i] = numColumn.get(i);
    }
    return res;
  }
  public Matrix stodmul(Matrix o)  {
    SparseMatrix res = new SparseMatrix(this.h, o.w);
    ArrayList<Double> tempdata = new ArrayList<>();
    ArrayList<Integer> numColumn = new ArrayList<>();
    ArrayList<Integer> ElementsInColumn = new ArrayList<>();
    ElementsInColumn.add(0);
    double tempel;
    int kol = 0;
    for (int i = 0; i < this.NumElInRow.length-1; i++) {
      int st = this.NumElInRow[i], fn = this.NumElInRow[i + 1];
      for (int j = 0; j < o.w; j++) {
        tempel = 0;
        for (int k = st; k < fn; k++) {
          tempel += this.elements[k] * o.getElement(this.columnIndex[k], j);
        }
        if (tempel != 0) {
          tempdata.add(tempel);
          numColumn.add(j);
          kol++;
        }
      }
      ElementsInColumn.add(kol);
    }
    res.elements = new double[tempdata.size()];
    res.columnIndex = new int[numColumn.size()];
    for (int i = 0; i < tempdata.size(); i++) {
      res.elements[i] = tempdata.get(i);
      res.columnIndex[i] = numColumn.get(i);
    }
    res.NumElInRow = new int[ElementsInColumn.size()];
    for (int i = 0; i < ElementsInColumn.size(); i++) {
      res.NumElInRow[i] = ElementsInColumn.get(i);
    }
    return res;
  }
  public Matrix stosmul(Matrix o) {
    SparseMatrix s=(SparseMatrix) o.transposition();
    SparseMatrix res=new SparseMatrix(this.h,s.h);
    ArrayList<Double> tempdata = new ArrayList<>();
    ArrayList<Integer> numColumn = new ArrayList<>();
    ArrayList<Integer> ElementsInColumn = new ArrayList<>();
    ElementsInColumn.add(0);
    int it1,it2,kol;
    double tempel;
    kol=0;
    for (int i = 0; i < this.NumElInRow.length-1; i++)
    {
      int st = this.NumElInRow[i], fn = this.NumElInRow[i + 1];
      for(int j=0;j<s.NumElInRow.length-1;j++)
      {
        int sts = s.NumElInRow[j], fns = s.NumElInRow[j + 1];
        tempel=0;
        it1 = st;
        it2 = sts;
        while ((it1 < fn) && (it2 < fns)) {
          if(this.columnIndex[it1]==s.columnIndex[it2])
          {
            tempel+=this.elements[it1]*s.elements[it2];
            it1++;
            it2++;
          }
          else if (this.columnIndex[it1]<s.columnIndex[it2])
          {
            it1++;
          }
          else
          {
            it2++;
          }
        }
        if(tempel!=0)
        {
          kol++;
          tempdata.add(tempel);
          numColumn.add(j);
        }
      }
      ElementsInColumn.add(kol);
    }
    res.elements = new double[tempdata.size()];
    res.columnIndex = new int[numColumn.size()];
    for (int i = 0; i < tempdata.size(); i++) {
      res.elements[i] = tempdata.get(i);
      res.columnIndex[i] = numColumn.get(i);
    }
    res.NumElInRow = new int[ElementsInColumn.size()];
    for (int i = 0; i < ElementsInColumn.size(); i++) {
      res.NumElInRow[i] = ElementsInColumn.get(i);
    }
    return res;
  }


  public String SMdebug() {
    StringBuilder alpha = new StringBuilder();
    alpha.append("El: ");
    for (double element : elements) {
      alpha.append(element).append(" ");
    }
    alpha.append("\n");
    alpha.append("NumColumns: ");
    for (int index : columnIndex) {
      alpha.append(index).append(" ");
    }
    alpha.append("\n");
    alpha.append("NumEl: ");
    for (int j : NumElInRow) {
      alpha.append(j).append(" ");
    }
    alpha.append("\n");
    return alpha.toString();
  }

  @Override
  public String toString() {
    StringBuilder alpha = new StringBuilder();
    for (int i = 0; i < h; i++) {
      int st = NumElInRow[i], fn = NumElInRow[i + 1];
      int tempnum = -1;
      for (int w = st; w < fn; w++) {
        while (columnIndex[w] - tempnum > 1) {
          alpha.append("0.0 ");
          tempnum++;
        }
        alpha.append(elements[w]).append(" ");
        tempnum++;
      }
      while (this.w - tempnum > 1) {
        alpha.append("0.0 ");
        tempnum++;
      }
      alpha.append("\n");
    }
    return alpha.toString();
  }

  @Override
  public double getElement(int i, int j) {
    int st = NumElInRow[i], fn = NumElInRow[i + 1];
    for (int w = st; w < fn; w++) {
      if (columnIndex[w] > j) return 0;
      else if (columnIndex[w] == j) return elements[w];
    }
    return 0;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof DenseMatrix) {
      return this.sdequals(o);
    } else {
      return this.ssequal(o);
    }
  }

  public boolean ssequal(Object o) {
    SparseMatrix s = (SparseMatrix) o;
    if ((this.h != s.h) || (this.w != s.w)) return false;
    return (Arrays.equals(this.elements, s.elements)) && (Arrays.equals(this.columnIndex, s.columnIndex))
            && (Arrays.equals(this.NumElInRow, s.NumElInRow));
  }

  public boolean sdequals(Object o) {
    DenseMatrix m = (DenseMatrix) o;
    if ((this.h != m.h) || (this.w != m.w)) return false;
    for (int i = 0; i < m.h; i++) {
      for (int j = 0; j < m.w; j++) {
        if (m.data[i][j] != this.getElement(i, j)) return false;
      }
    }
    return true;
  }
}
