<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--NewPage-->
<HTML>
<HEAD>
<!-- Generated by javadoc (build 1.6.0_0) on Tue Nov 03 18:15:10 EST 2009 -->
<TITLE>
CheckLevel (Compiler)
</TITLE>

<META NAME="date" CONTENT="2009-11-03">

<LINK REL ="stylesheet" TYPE="text/css" HREF="../../../../dev_javadoc.css" TITLE="Style">

<SCRIPT type="text/javascript">
function windowTitle()
{
    if (location.href.indexOf('is-external=true') == -1) {
        parent.document.title="CheckLevel (Compiler)";
    }
}
</SCRIPT>
<NOSCRIPT>
</NOSCRIPT>

</HEAD>

<BODY BGCOLOR="white" onload="windowTitle();">
<HR>


<!-- ========= START OF TOP NAVBAR ======= -->
<A NAME="navbar_top"><!-- --></A>
<A HREF="#skip-navbar_top" title="Skip navigation links"></A>
<TABLE BORDER="0" WIDTH="100%" CELLPADDING="1" CELLSPACING="0" SUMMARY="">
<TR>
<TD COLSPAN=2 BGCOLOR="#EEEEFF" CLASS="NavBarCell1">
<A NAME="navbar_top_firstrow"><!-- --></A>
<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="3" SUMMARY="">
  <TR ALIGN="center" VALIGN="top">
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <A HREF="../../../../overview-summary.html"><FONT CLASS="NavBarFont1"><B>Overview</B></FONT></A>&nbsp;</TD>
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <A HREF="package-summary.html"><FONT CLASS="NavBarFont1"><B>Package</B></FONT></A>&nbsp;</TD>
  <TD BGCOLOR="#FFFFFF" CLASS="NavBarCell1Rev"> &nbsp;<FONT CLASS="NavBarFont1Rev"><B>Class</B></FONT>&nbsp;</TD>
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <A HREF="package-tree.html"><FONT CLASS="NavBarFont1"><B>Tree</B></FONT></A>&nbsp;</TD>
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <A HREF="../../../../deprecated-list.html"><FONT CLASS="NavBarFont1"><B>Deprecated</B></FONT></A>&nbsp;</TD>
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <A HREF="../../../../index-all.html"><FONT CLASS="NavBarFont1"><B>Index</B></FONT></A>&nbsp;</TD>
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <A HREF="../../../../help-doc.html"><FONT CLASS="NavBarFont1"><B>Help</B></FONT></A>&nbsp;</TD>
  </TR>
</TABLE>
</TD>
<TD ALIGN="right" VALIGN="top" ROWSPAN=3><EM>
</EM>
</TD>
</TR>

<TR>
<TD BGCOLOR="white" CLASS="NavBarCell2"><FONT SIZE="-2">
&nbsp;<A HREF="../../../../com/google/javascript/jscomp/BasicErrorManager.html" title="class in com.google.javascript.jscomp"><B>PREV CLASS</B></A>&nbsp;
&nbsp;<A HREF="../../../../com/google/javascript/jscomp/CheckLevelLegacy.html" title="enum in com.google.javascript.jscomp"><B>NEXT CLASS</B></A></FONT></TD>
<TD BGCOLOR="white" CLASS="NavBarCell2"><FONT SIZE="-2">
  <A HREF="../../../../index.html?com/google/javascript/jscomp/CheckLevel.html" target="_top"><B>FRAMES</B></A>  &nbsp;
&nbsp;<A HREF="CheckLevel.html" target="_top"><B>NO FRAMES</B></A>  &nbsp;
&nbsp;<SCRIPT type="text/javascript">
  <!--
  if(window==top) {
    document.writeln('<A HREF="../../../../allclasses-noframe.html"><B>All Classes</B></A>');
  }
  //-->
</SCRIPT>
<NOSCRIPT>
  <A HREF="../../../../allclasses-noframe.html"><B>All Classes</B></A>
</NOSCRIPT>


</FONT></TD>
</TR>
<TR>
<TD VALIGN="top" CLASS="NavBarCell3"><FONT SIZE="-2">
  SUMMARY:&nbsp;NESTED&nbsp;|&nbsp;<A HREF="#enum_constant_summary">ENUM CONSTANTS</A>&nbsp;|&nbsp;FIELD&nbsp;|&nbsp;<A HREF="#method_summary">METHOD</A></FONT></TD>
<TD VALIGN="top" CLASS="NavBarCell3"><FONT SIZE="-2">
DETAIL:&nbsp;<A HREF="#enum_constant_detail">ENUM CONSTANTS</A>&nbsp;|&nbsp;FIELD&nbsp;|&nbsp;<A HREF="#method_detail">METHOD</A></FONT></TD>
</TR>
</TABLE>
<A NAME="skip-navbar_top"></A>
<!-- ========= END OF TOP NAVBAR ========= -->

<HR>
<!-- ======== START OF CLASS DATA ======== -->
<H2>
<FONT SIZE="-1">
com.google.javascript.jscomp</FONT>
<BR>
Enum CheckLevel</H2>
<PRE>
<A HREF="http://java.sun.com/javase/6/docs/api/java/lang/Object.html?is-external=true" title="class or interface in java.lang">java.lang.Object</A>
  <IMG SRC="../../../../resources/inherit.gif" ALT="extended by "><A HREF="http://java.sun.com/javase/6/docs/api/java/lang/Enum.html?is-external=true" title="class or interface in java.lang">java.lang.Enum</A>&lt;<A HREF="../../../../com/google/javascript/jscomp/CheckLevel.html" title="enum in com.google.javascript.jscomp">CheckLevel</A>&gt;
      <IMG SRC="../../../../resources/inherit.gif" ALT="extended by "><B>com.google.javascript.jscomp.CheckLevel</B>
</PRE>
<DL>
<DT><B>All Implemented Interfaces:</B> <DD><A HREF="http://java.sun.com/javase/6/docs/api/java/io/Serializable.html?is-external=true" title="class or interface in java.io">Serializable</A>, <A HREF="http://java.sun.com/javase/6/docs/api/java/lang/Comparable.html?is-external=true" title="class or interface in java.lang">Comparable</A>&lt;<A HREF="../../../../com/google/javascript/jscomp/CheckLevel.html" title="enum in com.google.javascript.jscomp">CheckLevel</A>&gt;</DD>
</DL>
<HR>
<DL>
<DT><PRE>public enum <B>CheckLevel</B><DT>extends <A HREF="http://java.sun.com/javase/6/docs/api/java/lang/Enum.html?is-external=true" title="class or interface in java.lang">Enum</A>&lt;<A HREF="../../../../com/google/javascript/jscomp/CheckLevel.html" title="enum in com.google.javascript.jscomp">CheckLevel</A>&gt;</DL>
</PRE>

<P>
Controls checking levels of certain options.  For all checks going
 forward, this should be used instead of booleans, so teams and
 individuals can control which checks are off, which produce only warnings,
 and which produce errors, without everyone having to agree.
<P>

<P>
<HR>

<P>
<!-- =========== ENUM CONSTANT SUMMARY =========== -->

<A NAME="enum_constant_summary"><!-- --></A>
<TABLE BORDER="1" WIDTH="100%" CELLPADDING="3" CELLSPACING="0" SUMMARY="">
<TR BGCOLOR="#CCCCFF" CLASS="TableHeadingColor">
<TH ALIGN="left" COLSPAN="2"><FONT SIZE="+2">
<B>Enum Constant Summary</B></FONT></TH>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD><CODE><B><A HREF="../../../../com/google/javascript/jscomp/CheckLevel.html#ERROR">ERROR</A></B></CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD><CODE><B><A HREF="../../../../com/google/javascript/jscomp/CheckLevel.html#OFF">OFF</A></B></CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD><CODE><B><A HREF="../../../../com/google/javascript/jscomp/CheckLevel.html#WARNING">WARNING</A></B></CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
</TR>
</TABLE>
&nbsp;
<!-- ========== METHOD SUMMARY =========== -->

<A NAME="method_summary"><!-- --></A>
<TABLE BORDER="1" WIDTH="100%" CELLPADDING="3" CELLSPACING="0" SUMMARY="">
<TR BGCOLOR="#CCCCFF" CLASS="TableHeadingColor">
<TH ALIGN="left" COLSPAN="2"><FONT SIZE="+2">
<B>Method Summary</B></FONT></TH>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD ALIGN="right" VALIGN="top" WIDTH="1%"><FONT SIZE="-1">
<CODE>static&nbsp;<A HREF="../../../../com/google/javascript/jscomp/CheckLevel.html" title="enum in com.google.javascript.jscomp">CheckLevel</A></CODE></FONT></TD>
<TD><CODE><B><A HREF="../../../../com/google/javascript/jscomp/CheckLevel.html#valueOf(java.lang.String)">valueOf</A></B>(<A HREF="http://java.sun.com/javase/6/docs/api/java/lang/String.html?is-external=true" title="class or interface in java.lang">String</A>&nbsp;name)</CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Returns the enum constant of this type with the specified name.</TD>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD ALIGN="right" VALIGN="top" WIDTH="1%"><FONT SIZE="-1">
<CODE>static&nbsp;<A HREF="../../../../com/google/javascript/jscomp/CheckLevel.html" title="enum in com.google.javascript.jscomp">CheckLevel</A>[]</CODE></FONT></TD>
<TD><CODE><B><A HREF="../../../../com/google/javascript/jscomp/CheckLevel.html#values()">values</A></B>()</CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Returns an array containing the constants of this enum type, in
the order they are declared.</TD>
</TR>
</TABLE>
&nbsp;<A NAME="methods_inherited_from_class_java.lang.Enum"><!-- --></A>
<TABLE BORDER="1" WIDTH="100%" CELLPADDING="3" CELLSPACING="0" SUMMARY="">
<TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
<TH ALIGN="left"><B>Methods inherited from class java.lang.<A HREF="http://java.sun.com/javase/6/docs/api/java/lang/Enum.html?is-external=true" title="class or interface in java.lang">Enum</A></B></TH>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD><CODE><A HREF="http://java.sun.com/javase/6/docs/api/java/lang/Enum.html?is-external=true#clone()" title="class or interface in java.lang">clone</A>, <A HREF="http://java.sun.com/javase/6/docs/api/java/lang/Enum.html?is-external=true#compareTo(E)" title="class or interface in java.lang">compareTo</A>, <A HREF="http://java.sun.com/javase/6/docs/api/java/lang/Enum.html?is-external=true#equals(java.lang.Object)" title="class or interface in java.lang">equals</A>, <A HREF="http://java.sun.com/javase/6/docs/api/java/lang/Enum.html?is-external=true#finalize()" title="class or interface in java.lang">finalize</A>, <A HREF="http://java.sun.com/javase/6/docs/api/java/lang/Enum.html?is-external=true#getDeclaringClass()" title="class or interface in java.lang">getDeclaringClass</A>, <A HREF="http://java.sun.com/javase/6/docs/api/java/lang/Enum.html?is-external=true#hashCode()" title="class or interface in java.lang">hashCode</A>, <A HREF="http://java.sun.com/javase/6/docs/api/java/lang/Enum.html?is-external=true#name()" title="class or interface in java.lang">name</A>, <A HREF="http://java.sun.com/javase/6/docs/api/java/lang/Enum.html?is-external=true#ordinal()" title="class or interface in java.lang">ordinal</A>, <A HREF="http://java.sun.com/javase/6/docs/api/java/lang/Enum.html?is-external=true#toString()" title="class or interface in java.lang">toString</A>, <A HREF="http://java.sun.com/javase/6/docs/api/java/lang/Enum.html?is-external=true#valueOf(java.lang.Class, java.lang.String)" title="class or interface in java.lang">valueOf</A></CODE></TD>
</TR>
</TABLE>
&nbsp;<A NAME="methods_inherited_from_class_java.lang.Object"><!-- --></A>
<TABLE BORDER="1" WIDTH="100%" CELLPADDING="3" CELLSPACING="0" SUMMARY="">
<TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
<TH ALIGN="left"><B>Methods inherited from class java.lang.<A HREF="http://java.sun.com/javase/6/docs/api/java/lang/Object.html?is-external=true" title="class or interface in java.lang">Object</A></B></TH>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD><CODE><A HREF="http://java.sun.com/javase/6/docs/api/java/lang/Object.html?is-external=true#getClass()" title="class or interface in java.lang">getClass</A>, <A HREF="http://java.sun.com/javase/6/docs/api/java/lang/Object.html?is-external=true#notify()" title="class or interface in java.lang">notify</A>, <A HREF="http://java.sun.com/javase/6/docs/api/java/lang/Object.html?is-external=true#notifyAll()" title="class or interface in java.lang">notifyAll</A>, <A HREF="http://java.sun.com/javase/6/docs/api/java/lang/Object.html?is-external=true#wait()" title="class or interface in java.lang">wait</A>, <A HREF="http://java.sun.com/javase/6/docs/api/java/lang/Object.html?is-external=true#wait(long)" title="class or interface in java.lang">wait</A>, <A HREF="http://java.sun.com/javase/6/docs/api/java/lang/Object.html?is-external=true#wait(long, int)" title="class or interface in java.lang">wait</A></CODE></TD>
</TR>
</TABLE>
&nbsp;
<P>

<!-- ============ ENUM CONSTANT DETAIL =========== -->

<A NAME="enum_constant_detail"><!-- --></A>
<TABLE BORDER="1" WIDTH="100%" CELLPADDING="3" CELLSPACING="0" SUMMARY="">
<TR BGCOLOR="#CCCCFF" CLASS="TableHeadingColor">
<TH ALIGN="left" COLSPAN="1"><FONT SIZE="+2">
<B>Enum Constant Detail</B></FONT></TH>
</TR>
</TABLE>

<A NAME="ERROR"><!-- --></A><H3>
ERROR</H3>
<PRE>
public static final <A HREF="../../../../com/google/javascript/jscomp/CheckLevel.html" title="enum in com.google.javascript.jscomp">CheckLevel</A> <B>ERROR</B></PRE>
<DL>
<DL>
</DL>
</DL>
<HR>

<A NAME="WARNING"><!-- --></A><H3>
WARNING</H3>
<PRE>
public static final <A HREF="../../../../com/google/javascript/jscomp/CheckLevel.html" title="enum in com.google.javascript.jscomp">CheckLevel</A> <B>WARNING</B></PRE>
<DL>
<DL>
</DL>
</DL>
<HR>

<A NAME="OFF"><!-- --></A><H3>
OFF</H3>
<PRE>
public static final <A HREF="../../../../com/google/javascript/jscomp/CheckLevel.html" title="enum in com.google.javascript.jscomp">CheckLevel</A> <B>OFF</B></PRE>
<DL>
<DL>
</DL>
</DL>

<!-- ============ METHOD DETAIL ========== -->

<A NAME="method_detail"><!-- --></A>
<TABLE BORDER="1" WIDTH="100%" CELLPADDING="3" CELLSPACING="0" SUMMARY="">
<TR BGCOLOR="#CCCCFF" CLASS="TableHeadingColor">
<TH ALIGN="left" COLSPAN="1"><FONT SIZE="+2">
<B>Method Detail</B></FONT></TH>
</TR>
</TABLE>

<A NAME="values()"><!-- --></A><H3>
values</H3>
<PRE>
public static <A HREF="../../../../com/google/javascript/jscomp/CheckLevel.html" title="enum in com.google.javascript.jscomp">CheckLevel</A>[] <B>values</B>()</PRE>
<DL>
<DD>Returns an array containing the constants of this enum type, in
the order they are declared.  This method may be used to iterate
over the constants as follows:
<pre>
for (CheckLevel c : CheckLevel.values())
&nbsp;   System.out.println(c);
</pre>
<P>
<DD><DL>

<DT><B>Returns:</B><DD>an array containing the constants of this enum type, in
the order they are declared</DL>
</DD>
</DL>
<HR>

<A NAME="valueOf(java.lang.String)"><!-- --></A><H3>
valueOf</H3>
<PRE>
public static <A HREF="../../../../com/google/javascript/jscomp/CheckLevel.html" title="enum in com.google.javascript.jscomp">CheckLevel</A> <B>valueOf</B>(<A HREF="http://java.sun.com/javase/6/docs/api/java/lang/String.html?is-external=true" title="class or interface in java.lang">String</A>&nbsp;name)</PRE>
<DL>
<DD>Returns the enum constant of this type with the specified name.
The string must match <I>exactly</I> an identifier used to declare an
enum constant in this type.  (Extraneous whitespace characters are 
not permitted.)
<P>
<DD><DL>
<DT><B>Parameters:</B><DD><CODE>name</CODE> - the name of the enum constant to be returned.
<DT><B>Returns:</B><DD>the enum constant with the specified name
<DT><B>Throws:</B>
<DD><CODE><A HREF="http://java.sun.com/javase/6/docs/api/java/lang/IllegalArgumentException.html?is-external=true" title="class or interface in java.lang">IllegalArgumentException</A></CODE> - if this enum type has no constant
with the specified name
<DD><CODE><A HREF="http://java.sun.com/javase/6/docs/api/java/lang/NullPointerException.html?is-external=true" title="class or interface in java.lang">NullPointerException</A></CODE> - if the argument is null</DL>
</DD>
</DL>
<!-- ========= END OF CLASS DATA ========= -->
<HR>


<!-- ======= START OF BOTTOM NAVBAR ====== -->
<A NAME="navbar_bottom"><!-- --></A>
<A HREF="#skip-navbar_bottom" title="Skip navigation links"></A>
<TABLE BORDER="0" WIDTH="100%" CELLPADDING="1" CELLSPACING="0" SUMMARY="">
<TR>
<TD COLSPAN=2 BGCOLOR="#EEEEFF" CLASS="NavBarCell1">
<A NAME="navbar_bottom_firstrow"><!-- --></A>
<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="3" SUMMARY="">
  <TR ALIGN="center" VALIGN="top">
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <A HREF="../../../../overview-summary.html"><FONT CLASS="NavBarFont1"><B>Overview</B></FONT></A>&nbsp;</TD>
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <A HREF="package-summary.html"><FONT CLASS="NavBarFont1"><B>Package</B></FONT></A>&nbsp;</TD>
  <TD BGCOLOR="#FFFFFF" CLASS="NavBarCell1Rev"> &nbsp;<FONT CLASS="NavBarFont1Rev"><B>Class</B></FONT>&nbsp;</TD>
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <A HREF="package-tree.html"><FONT CLASS="NavBarFont1"><B>Tree</B></FONT></A>&nbsp;</TD>
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <A HREF="../../../../deprecated-list.html"><FONT CLASS="NavBarFont1"><B>Deprecated</B></FONT></A>&nbsp;</TD>
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <A HREF="../../../../index-all.html"><FONT CLASS="NavBarFont1"><B>Index</B></FONT></A>&nbsp;</TD>
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <A HREF="../../../../help-doc.html"><FONT CLASS="NavBarFont1"><B>Help</B></FONT></A>&nbsp;</TD>
  </TR>
</TABLE>
</TD>
<TD ALIGN="right" VALIGN="top" ROWSPAN=3><EM>
</EM>
</TD>
</TR>

<TR>
<TD BGCOLOR="white" CLASS="NavBarCell2"><FONT SIZE="-2">
&nbsp;<A HREF="../../../../com/google/javascript/jscomp/BasicErrorManager.html" title="class in com.google.javascript.jscomp"><B>PREV CLASS</B></A>&nbsp;
&nbsp;<A HREF="../../../../com/google/javascript/jscomp/CheckLevelLegacy.html" title="enum in com.google.javascript.jscomp"><B>NEXT CLASS</B></A></FONT></TD>
<TD BGCOLOR="white" CLASS="NavBarCell2"><FONT SIZE="-2">
  <A HREF="../../../../index.html?com/google/javascript/jscomp/CheckLevel.html" target="_top"><B>FRAMES</B></A>  &nbsp;
&nbsp;<A HREF="CheckLevel.html" target="_top"><B>NO FRAMES</B></A>  &nbsp;
&nbsp;<SCRIPT type="text/javascript">
  <!--
  if(window==top) {
    document.writeln('<A HREF="../../../../allclasses-noframe.html"><B>All Classes</B></A>');
  }
  //-->
</SCRIPT>
<NOSCRIPT>
  <A HREF="../../../../allclasses-noframe.html"><B>All Classes</B></A>
</NOSCRIPT>


</FONT></TD>
</TR>
<TR>
<TD VALIGN="top" CLASS="NavBarCell3"><FONT SIZE="-2">
  SUMMARY:&nbsp;NESTED&nbsp;|&nbsp;<A HREF="#enum_constant_summary">ENUM CONSTANTS</A>&nbsp;|&nbsp;FIELD&nbsp;|&nbsp;<A HREF="#method_summary">METHOD</A></FONT></TD>
<TD VALIGN="top" CLASS="NavBarCell3"><FONT SIZE="-2">
DETAIL:&nbsp;<A HREF="#enum_constant_detail">ENUM CONSTANTS</A>&nbsp;|&nbsp;FIELD&nbsp;|&nbsp;<A HREF="#method_detail">METHOD</A></FONT></TD>
</TR>
</TABLE>
<A NAME="skip-navbar_bottom"></A>
<!-- ======== END OF BOTTOM NAVBAR ======= -->

<HR>

        <div id="footer">
          <div id="footerlogo">
            <img src="http://www.google.com/images/art.gif"
                 alt="Google colored balls">
          </div>

          <div id="copyright">
          <p>&copy; 2009 Google -
            <a href="http://www.google.com/privacy.html">Privacy Policy</a> -
            <a href="http://www.google.com/terms_of_service.html">Terms and Conditions</a> -
            <a href="http://www.google.com/about.html">About Google</a>
          </p>
          </div>
        </div>
      
      
</BODY>
</HTML>
