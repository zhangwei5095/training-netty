/**
 * UDP协议开发<br>
 * 
 * <pre>
 * UDP是用户数据报协议(User Datagram Protocal, UDP)的简称，其主要作用是将网络数据
 * 流量压缩成数据报形式，提供面向事务的简单信息传送服务。与TCP协议不同，
 * UDP协议直接利用IP协议进行UDP数据报传送，UDP协议的面向无连接的、不可靠的数据报
 * 投递服务。当使用UDP协议传输信息时，用户应用程序必须负责解决数据报丢失、重复、排序、差错确认等问题。
 * 
 * 由于UDP具有资源消耗小、处理速度快的优点，所以通常视频、音频等可靠性要求不高
 * 的数据传输一般会使用UDP，即便有一定的丢包率，也不会对功能造成严重的影响。
 * 
 * 
 * 
 * </pre>
 * 
 * @author percy
 *
 */
package com.shangpin.netty.chapter12;