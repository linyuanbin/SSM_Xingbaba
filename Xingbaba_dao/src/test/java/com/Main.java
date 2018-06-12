package com;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
 
public class Main {
 
  public final static long MOD = 1000000000 + 7;

  public static long gcd(long a,long b){ //最大公约数
    return (a % b == 0) ? b : gcd(b,a%b);
  }
 
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    long n = in.nextInt();
    long ans = (long)1*n*(n*2-1) % MOD;     //n*n + n*(n-1)  = 2n^2-n
    Set<Integer> set  = new HashSet<>();
    for (int i = 2; i*i <= n; i++){
      if ( set.contains(i)) continue;
      long tmp = i;
      int cnt = 0;
 
      while(tmp <= n) {
        set.add((int)tmp);
        tmp = tmp * i;
        cnt++;
      }
      System.out.println(set.toString());
 
      for(int k = 1; k <= cnt; k++) {
        for(int j = k + 1; j <= cnt; j++) {
          ans = (ans + n / (j / gcd(k, j) ) * (long)2 ) % MOD;
        }
      }
    }
    System.out.println(ans);
  }
}

/*
package com;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    while(sc.hasNext()){
     int n = sc.nextInt();
      System.out.println(MaxGroup(n));
      }
    sc.close();
    }
  public static int MaxGroup(int n){
    if(n==1)
    return 1;
    else
    return MaxGroup(n-1)+4*n-3+Special(n);
    }

          public static int Special(int n){
    int number = 0;
    for(int i=2; i<=n/2; i++){
     for(int j=2; j<=n/2 ; j++){
      if(Math.pow(i, j) == n){
        number++;
       }
    }
    }
  return number;
   }
}*/
