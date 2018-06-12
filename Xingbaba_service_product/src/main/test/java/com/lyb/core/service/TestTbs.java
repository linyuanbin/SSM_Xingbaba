package com.lyb.core.service;

import com.lyb.core.bean.TestTb;
import com.lyb.core.bean.product.Brand;
import com.lyb.core.dao.TestTbDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by lin on 2018/3/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class TestTbs {

    @Autowired
    private TestTbDao testTbDao;

    @Autowired
    private TestTbService testTbService;

    @Test
    public void test() throws Exception {

        TestTb tt = new TestTb();
        tt.setName("老里4");
        tt.setBirthday(new Date());
        testTbService.insertTetstTb(tt);
    }

    @Test
    public void test01() {
        Integer first = new Integer(3);
        Integer second = 3;
        int three = 3;
        System.out.println(first == second);
        // System.out.println(first==three);
    }

    public static void main(String[] args) {
     /*   Integer first = new Integer(3);
        Integer second = 3;
        int three = 3;
        System.out.println(first==second);
        System.out.println(first==three);*/
      /*  int a=10,b=4,c=5,d=9;
        System.out.println(++a*b+c*--d);*/


      /* boolean a=true;
        boolean b=true;
        if(a==b){
            System.out.println("ds");
        }*/

        //不区分大小写的字符串查找
     /*   Scanner scanner=new Scanner(System.in); //a 97  z 122    A 65 Z 90     差 32
        String s="abcdEfGHi";
        String str=scanner.nextLine();
       // System.out.println(s.length());
        int status=0;
        for(int i=0;i<s.length()-str.length()+1;i++){
            status=0;
            int n=i,p=i;
            for(int j=0;j<str.length();j++){
                System.out.println("s:"+s.codePointAt(n)+" str:"+str.codePointAt(j));
                if(s.codePointAt(n)==str.codePointAt(j)||s.codePointAt(n)==(str.codePointAt(j)+32)){
                    status++;
                }else {
                    break;
                }
                n++;
            }
            if(status==str.length()){
                System.out.println("已找到 输入原字符串：");
                for(int l=0;l<str.length();l++) {
                    System.out.print(s.charAt(p++));
                }
                System.out.println();
                break;
            }else {
                System.out.println("没找到 继续");
            }
        }*/


    }
}
/*
#include<bits/stdc++.h>
        using namespace std;
        #define LL long long
        int main(){
        LL n,y;
        scanf("%lld",&n);
        char s[100];
        int k=0;
        LL t=n;
        while(t){
        k++;
        if(t%2==1){
        y=k;
        break;
        }
        t>>=1;
        }
        if(n%2)
        printf("No\n");
        else {
        LL c=(y-1)<<1;
        printf("%lld %lld\n",n/c,c);
        }
        return 0;

        }
*/
