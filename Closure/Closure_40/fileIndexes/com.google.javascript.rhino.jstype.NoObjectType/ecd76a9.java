<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--NewPage-->
<HTML>
<HEAD>
<TITLE>
NoObjectType (Compiler)
</TITLE>


<LINK REL ="stylesheet" TYPE="text/css" HREF="../../../../../dev_javadoc.css" TITLE="Style">

<SCRIPT type="text/javascript">
function windowTitle()
{
    if (location.href.indexOf('is-external=true') == -1) {
        parent.document.title="NoObjectType (Compiler)";
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
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <A HREF="../../../../../overview-summary.html"><FONT CLASS="NavBarFont1"><B>Overview</B></FONT></A>&nbsp;</TD>
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <A HREF="package-summary.html"><FONT CLASS="NavBarFont1"><B>Package</B></FONT></A>&nbsp;</TD>
  <TD BGCOLOR="#FFFFFF" CLASS="NavBarCell1Rev"> &nbsp;<FONT CLASS="NavBarFont1Rev"><B>Class</B></FONT>&nbsp;</TD>
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <A HREF="package-tree.html"><FONT CLASS="NavBarFont1"><B>Tree</B></FONT></A>&nbsp;</TD>
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <A HREF="../../../../../deprecated-list.html"><FONT CLASS="NavBarFont1"><B>Deprecated</B></FONT></A>&nbsp;</TD>
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <A HREF="../../../../../index-all.html"><FONT CLASS="NavBarFont1"><B>Index</B></FONT></A>&nbsp;</TD>
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <A HREF="../../../../../help-doc.html"><FONT CLASS="NavBarFont1"><B>Help</B></FONT></A>&nbsp;</TD>
  </TR>
</TABLE>
</TD>
<TD ALIGN="right" VALIGN="top" ROWSPAN=3><EM>
</EM>
</TD>
</TR>

<TR>
<TD BGCOLOR="white" CLASS="NavBarCell2"><FONT SIZE="-2">
&nbsp;<A HREF="../../../../../com/google/javascript/rhino/jstype/NamedType.html" title="class in com.google.javascript.rhino.jstype"><B>PREV CLASS</B></A>&nbsp;
&nbsp;<A HREF="../../../../../com/google/javascript/rhino/jstype/NoType.html" title="class in com.google.javascript.rhino.jstype"><B>NEXT CLASS</B></A></FONT></TD>
<TD BGCOLOR="white" CLASS="NavBarCell2"><FONT SIZE="-2">
  <A HREF="../../../../../index.html?com/google/javascript/rhino/jstype/NoObjectType.html" target="_top"><B>FRAMES</B></A>  &nbsp;
&nbsp;<A HREF="NoObjectType.html" target="_top"><B>NO FRAMES</B></A>  &nbsp;
&nbsp;<SCRIPT type="text/javascript">
  <!--
  if(window==top) {
    document.writeln('<A HREF="../../../../../allclasses-noframe.html"><B>All Classes</B></A>');
  }
  //-->
</SCRIPT>
<NOSCRIPT>
  <A HREF="../../../../../allclasses-noframe.html"><B>All Classes</B></A>
</NOSCRIPT>


</FONT></TD>
</TR>
<TR>
<TD VALIGN="top" CLASS="NavBarCell3"><FONT SIZE="-2">
  SUMMARY:&nbsp;NESTED&nbsp;|&nbsp;<A HREF="#fields_inherited_from_class_com.google.javascript.rhino.jstype.JSType">FIELD</A>&nbsp;|&nbsp;CONSTR&nbsp;|&nbsp;<A HREF="#method_summary">METHOD</A></FONT></TD>
<TD VALIGN="top" CLASS="NavBarCell3"><FONT SIZE="-2">
DETAIL:&nbsp;FIELD&nbsp;|&nbsp;CONSTR&nbsp;|&nbsp;<A HREF="#method_detail">METHOD</A></FONT></TD>
</TR>
</TABLE>
<A NAME="skip-navbar_top"></A>
<!-- ========= END OF TOP NAVBAR ========= -->

<HR>
<!-- ======== START OF CLASS DATA ======== -->
<H2>
<FONT SIZE="-1">
com.google.javascript.rhino.jstype</FONT>
<BR>
Class NoObjectType</H2>
<PRE>
<A HREF="http://java.sun.com/javase/6/docs/api/java/lang/Object.html?is-external=true" title="class or interface in java.lang">java.lang.Object</A>
  <IMG SRC="../../../../../resources/inherit.gif" ALT="extended by "><A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html" title="class in com.google.javascript.rhino.jstype">com.google.javascript.rhino.jstype.JSType</A>
      <IMG SRC="../../../../../resources/inherit.gif" ALT="extended by "><A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html" title="class in com.google.javascript.rhino.jstype">com.google.javascript.rhino.jstype.ObjectType</A>
          <IMG SRC="../../../../../resources/inherit.gif" ALT="extended by "><A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html" title="class in com.google.javascript.rhino.jstype">com.google.javascript.rhino.jstype.FunctionType</A>
              <IMG SRC="../../../../../resources/inherit.gif" ALT="extended by "><B>com.google.javascript.rhino.jstype.NoObjectType</B>
</PRE>
<DL>
<DT><B>All Implemented Interfaces:</B> <DD><A HREF="http://java.sun.com/javase/6/docs/api/java/io/Serializable.html?is-external=true" title="class or interface in java.io">Serializable</A></DD>
</DL>
<DL>
<DT><B>Direct Known Subclasses:</B> <DD><A HREF="../../../../../com/google/javascript/rhino/jstype/NoType.html" title="class in com.google.javascript.rhino.jstype">NoType</A></DD>
</DL>
<HR>
<DL>
<DT><PRE>public class <B>NoObjectType</B><DT>extends <A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html" title="class in com.google.javascript.rhino.jstype">FunctionType</A></DL>
</PRE>

<P>
The bottom Object type, representing the subclass of all objects.

 Although JavaScript programmers can't explicitly denote the bottom
 Object type, it comes up in static analysis. For example, if we have:
 <code>
 var x = function() {};
 if (x instanceof Array) {
   f(x);
 }
 </code>
 We need to be able to assign <code>x</code> a type within the <code>f(x)</code>
 call. It has no possible type, but <code>x</code> would not be legal if f
 expected a string. So we assign it the <code>NoObjectType</code>.
<P>

<P>
<DL>
<DT><B>See Also:</B><DD><a href="http://en.wikipedia.org/wiki/Bottom_type">Bottom types</a>, 
<A HREF="../../../../../serialized-form.html#com.google.javascript.rhino.jstype.NoObjectType">Serialized Form</A></DL>
<HR>

<P>
<!-- =========== FIELD SUMMARY =========== -->

<A NAME="field_summary"><!-- --></A>
<TABLE BORDER="1" WIDTH="100%" CELLPADDING="3" CELLSPACING="0" SUMMARY="">
<TR BGCOLOR="#CCCCFF" CLASS="TableHeadingColor">
<TH ALIGN="left" COLSPAN="2"><FONT SIZE="+2">
<B>Field Summary</B></FONT></TH>
</TR>
</TABLE>
&nbsp;<A NAME="fields_inherited_from_class_com.google.javascript.rhino.jstype.JSType"><!-- --></A>
<TABLE BORDER="1" WIDTH="100%" CELLPADDING="3" CELLSPACING="0" SUMMARY="">
<TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
<TH ALIGN="left"><B>Fields inherited from class com.google.javascript.rhino.jstype.<A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html" title="class in com.google.javascript.rhino.jstype">JSType</A></B></TH>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD><CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#EMPTY_TYPE_COMPONENT">EMPTY_TYPE_COMPONENT</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#ENUMDECL">ENUMDECL</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#NOT_A_CLASS">NOT_A_CLASS</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#NOT_A_TYPE">NOT_A_TYPE</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#NOT_ENUMDECL">NOT_ENUMDECL</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#UNKNOWN_NAME">UNKNOWN_NAME</A></CODE></TD>
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
<CODE>protected &nbsp;void</CODE></FONT></TD>
<TD><CODE><B><A HREF="../../../../../com/google/javascript/rhino/jstype/NoObjectType.html#collectPropertyNames(java.util.Set)">collectPropertyNames</A></B>(<A HREF="http://java.sun.com/javase/6/docs/api/java/util/Set.html?is-external=true" title="class or interface in java.util">Set</A>&lt;<A HREF="http://java.sun.com/javase/6/docs/api/java/lang/String.html?is-external=true" title="class or interface in java.lang">String</A>&gt;&nbsp;props)</CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Adds any properties defined on this type or its supertypes to the set.</TD>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD ALIGN="right" VALIGN="top" WIDTH="1%"><FONT SIZE="-1">
<CODE>&nbsp;boolean</CODE></FONT></TD>
<TD><CODE><B><A HREF="../../../../../com/google/javascript/rhino/jstype/NoObjectType.html#equals(java.lang.Object)">equals</A></B>(<A HREF="http://java.sun.com/javase/6/docs/api/java/lang/Object.html?is-external=true" title="class or interface in java.lang">Object</A>&nbsp;that)</CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Two function types are equal if their signatures match.</TD>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD ALIGN="right" VALIGN="top" WIDTH="1%"><FONT SIZE="-1">
<CODE>&nbsp;<A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html" title="class in com.google.javascript.rhino.jstype">FunctionType</A></CODE></FONT></TD>
<TD><CODE><B><A HREF="../../../../../com/google/javascript/rhino/jstype/NoObjectType.html#getConstructor()">getConstructor</A></B>()</CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Gets this object's constructor.</TD>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD ALIGN="right" VALIGN="top" WIDTH="1%"><FONT SIZE="-1">
<CODE>&nbsp;<A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html" title="class in com.google.javascript.rhino.jstype">JSType</A></CODE></FONT></TD>
<TD><CODE><B><A HREF="../../../../../com/google/javascript/rhino/jstype/NoObjectType.html#getGreatestSubtype(com.google.javascript.rhino.jstype.JSType)">getGreatestSubtype</A></B>(<A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html" title="class in com.google.javascript.rhino.jstype">JSType</A>&nbsp;that)</CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Gets the greatest subtype of <code>this</code> and <code>that</code>.</TD>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD ALIGN="right" VALIGN="top" WIDTH="1%"><FONT SIZE="-1">
<CODE>&nbsp;<A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html" title="class in com.google.javascript.rhino.jstype">ObjectType</A></CODE></FONT></TD>
<TD><CODE><B><A HREF="../../../../../com/google/javascript/rhino/jstype/NoObjectType.html#getImplicitPrototype()">getImplicitPrototype</A></B>()</CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Gets the implicit prototype (a.k.a.</TD>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD ALIGN="right" VALIGN="top" WIDTH="1%"><FONT SIZE="-1">
<CODE>&nbsp;<A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html" title="class in com.google.javascript.rhino.jstype">ObjectType</A></CODE></FONT></TD>
<TD><CODE><B><A HREF="../../../../../com/google/javascript/rhino/jstype/NoObjectType.html#getInstanceType()">getInstanceType</A></B>()</CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Gets the type of instance of this function.</TD>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD ALIGN="right" VALIGN="top" WIDTH="1%"><FONT SIZE="-1">
<CODE>&nbsp;<A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html" title="class in com.google.javascript.rhino.jstype">JSType</A></CODE></FONT></TD>
<TD><CODE><B><A HREF="../../../../../com/google/javascript/rhino/jstype/NoObjectType.html#getLeastSupertype(com.google.javascript.rhino.jstype.JSType)">getLeastSupertype</A></B>(<A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html" title="class in com.google.javascript.rhino.jstype">JSType</A>&nbsp;that)</CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Gets the least supertype of <code>this</code> and <code>that</code>.</TD>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD ALIGN="right" VALIGN="top" WIDTH="1%"><FONT SIZE="-1">
<CODE>&nbsp;<A HREF="../../../../../com/google/javascript/rhino/JSDocInfo.html" title="class in com.google.javascript.rhino">JSDocInfo</A></CODE></FONT></TD>
<TD><CODE><B><A HREF="../../../../../com/google/javascript/rhino/jstype/NoObjectType.html#getOwnPropertyJSDocInfo(java.lang.String)">getOwnPropertyJSDocInfo</A></B>(<A HREF="http://java.sun.com/javase/6/docs/api/java/lang/String.html?is-external=true" title="class or interface in java.lang">String</A>&nbsp;propertyName)</CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Gets the docInfo on the specified property on this type.</TD>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD ALIGN="right" VALIGN="top" WIDTH="1%"><FONT SIZE="-1">
<CODE>&nbsp;<A HREF="http://java.sun.com/javase/6/docs/api/java/util/Set.html?is-external=true" title="class or interface in java.util">Set</A>&lt;<A HREF="http://java.sun.com/javase/6/docs/api/java/lang/String.html?is-external=true" title="class or interface in java.lang">String</A>&gt;</CODE></FONT></TD>
<TD><CODE><B><A HREF="../../../../../com/google/javascript/rhino/jstype/NoObjectType.html#getOwnPropertyNames()">getOwnPropertyNames</A></B>()</CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Returns the names of all the properties directly on this type.</TD>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD ALIGN="right" VALIGN="top" WIDTH="1%"><FONT SIZE="-1">
<CODE>&nbsp;int</CODE></FONT></TD>
<TD><CODE><B><A HREF="../../../../../com/google/javascript/rhino/jstype/NoObjectType.html#getPropertiesCount()">getPropertiesCount</A></B>()</CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Gets the number of properties of this object.</TD>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD ALIGN="right" VALIGN="top" WIDTH="1%"><FONT SIZE="-1">
<CODE>&nbsp;<A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html" title="class in com.google.javascript.rhino.jstype">JSType</A></CODE></FONT></TD>
<TD><CODE><B><A HREF="../../../../../com/google/javascript/rhino/jstype/NoObjectType.html#getPropertyType(java.lang.String)">getPropertyType</A></B>(<A HREF="http://java.sun.com/javase/6/docs/api/java/lang/String.html?is-external=true" title="class or interface in java.lang">String</A>&nbsp;propertyName)</CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Gets the property type of the property whose name is given.</TD>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD ALIGN="right" VALIGN="top" WIDTH="1%"><FONT SIZE="-1">
<CODE>&nbsp;<A HREF="http://java.sun.com/javase/6/docs/api/java/lang/String.html?is-external=true" title="class or interface in java.lang">String</A></CODE></FONT></TD>
<TD><CODE><B><A HREF="../../../../../com/google/javascript/rhino/jstype/NoObjectType.html#getReferenceName()">getReferenceName</A></B>()</CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Gets the reference name for this object.</TD>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD ALIGN="right" VALIGN="top" WIDTH="1%"><FONT SIZE="-1">
<CODE>&nbsp;<A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html" title="class in com.google.javascript.rhino.jstype">JSType</A></CODE></FONT></TD>
<TD><CODE><B><A HREF="../../../../../com/google/javascript/rhino/jstype/NoObjectType.html#getReturnType()">getReturnType</A></B>()</CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD ALIGN="right" VALIGN="top" WIDTH="1%"><FONT SIZE="-1">
<CODE>&nbsp;int</CODE></FONT></TD>
<TD><CODE><B><A HREF="../../../../../com/google/javascript/rhino/jstype/NoObjectType.html#hashCode()">hashCode</A></B>()</CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD ALIGN="right" VALIGN="top" WIDTH="1%"><FONT SIZE="-1">
<CODE>&nbsp;boolean</CODE></FONT></TD>
<TD><CODE><B><A HREF="../../../../../com/google/javascript/rhino/jstype/NoObjectType.html#hasOwnProperty(java.lang.String)">hasOwnProperty</A></B>(<A HREF="http://java.sun.com/javase/6/docs/api/java/lang/String.html?is-external=true" title="class or interface in java.lang">String</A>&nbsp;propertyName)</CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Checks whether the property whose name is given is present directly on
 the object.</TD>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD ALIGN="right" VALIGN="top" WIDTH="1%"><FONT SIZE="-1">
<CODE>&nbsp;boolean</CODE></FONT></TD>
<TD><CODE><B><A HREF="../../../../../com/google/javascript/rhino/jstype/NoObjectType.html#hasProperty(java.lang.String)">hasProperty</A></B>(<A HREF="http://java.sun.com/javase/6/docs/api/java/lang/String.html?is-external=true" title="class or interface in java.lang">String</A>&nbsp;propertyName)</CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Checks whether the property whose name is given is present on the
 object.</TD>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD ALIGN="right" VALIGN="top" WIDTH="1%"><FONT SIZE="-1">
<CODE>&nbsp;boolean</CODE></FONT></TD>
<TD><CODE><B><A HREF="../../../../../com/google/javascript/rhino/jstype/NoObjectType.html#hasReferenceName()">hasReferenceName</A></B>()</CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Returns true if the object is named.</TD>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD ALIGN="right" VALIGN="top" WIDTH="1%"><FONT SIZE="-1">
<CODE>&nbsp;boolean</CODE></FONT></TD>
<TD><CODE><B><A HREF="../../../../../com/google/javascript/rhino/jstype/NoObjectType.html#isFunctionType()">isFunctionType</A></B>()</CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD ALIGN="right" VALIGN="top" WIDTH="1%"><FONT SIZE="-1">
<CODE>&nbsp;boolean</CODE></FONT></TD>
<TD><CODE><B><A HREF="../../../../../com/google/javascript/rhino/jstype/NoObjectType.html#isNativeObjectType()">isNativeObjectType</A></B>()</CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Whether this is a built-in object.</TD>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD ALIGN="right" VALIGN="top" WIDTH="1%"><FONT SIZE="-1">
<CODE>&nbsp;boolean</CODE></FONT></TD>
<TD><CODE><B><A HREF="../../../../../com/google/javascript/rhino/jstype/NoObjectType.html#isNoObjectType()">isNoObjectType</A></B>()</CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD ALIGN="right" VALIGN="top" WIDTH="1%"><FONT SIZE="-1">
<CODE>&nbsp;boolean</CODE></FONT></TD>
<TD><CODE><B><A HREF="../../../../../com/google/javascript/rhino/jstype/NoObjectType.html#isPropertyInExterns(java.lang.String)">isPropertyInExterns</A></B>(<A HREF="http://java.sun.com/javase/6/docs/api/java/lang/String.html?is-external=true" title="class or interface in java.lang">String</A>&nbsp;propertyName)</CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Checks whether the property was defined in the externs.</TD>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD ALIGN="right" VALIGN="top" WIDTH="1%"><FONT SIZE="-1">
<CODE>&nbsp;boolean</CODE></FONT></TD>
<TD><CODE><B><A HREF="../../../../../com/google/javascript/rhino/jstype/NoObjectType.html#isPropertyTypeDeclared(java.lang.String)">isPropertyTypeDeclared</A></B>(<A HREF="http://java.sun.com/javase/6/docs/api/java/lang/String.html?is-external=true" title="class or interface in java.lang">String</A>&nbsp;property)</CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Checks whether the property's type is declared.</TD>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD ALIGN="right" VALIGN="top" WIDTH="1%"><FONT SIZE="-1">
<CODE>&nbsp;boolean</CODE></FONT></TD>
<TD><CODE><B><A HREF="../../../../../com/google/javascript/rhino/jstype/NoObjectType.html#isPropertyTypeInferred(java.lang.String)">isPropertyTypeInferred</A></B>(<A HREF="http://java.sun.com/javase/6/docs/api/java/lang/String.html?is-external=true" title="class or interface in java.lang">String</A>&nbsp;propertyName)</CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Checks whether the property's type is inferred.</TD>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD ALIGN="right" VALIGN="top" WIDTH="1%"><FONT SIZE="-1">
<CODE>&nbsp;boolean</CODE></FONT></TD>
<TD><CODE><B><A HREF="../../../../../com/google/javascript/rhino/jstype/NoObjectType.html#isSubtype(com.google.javascript.rhino.jstype.JSType)">isSubtype</A></B>(<A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html" title="class in com.google.javascript.rhino.jstype">JSType</A>&nbsp;that)</CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;A function is a subtype of another if their call methods are related via
 subtyping and <code>this</code> is a subtype of <code>that</code> with regard to
 the prototype chain.</TD>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD ALIGN="right" VALIGN="top" WIDTH="1%"><FONT SIZE="-1">
<CODE>&nbsp;boolean</CODE></FONT></TD>
<TD><CODE><B><A HREF="../../../../../com/google/javascript/rhino/jstype/NoObjectType.html#matchesNumberContext()">matchesNumberContext</A></B>()</CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;This predicate is used to test whether a given type can appear in a
 numeric context, such as an operand of a multiply operator.</TD>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD ALIGN="right" VALIGN="top" WIDTH="1%"><FONT SIZE="-1">
<CODE>&nbsp;boolean</CODE></FONT></TD>
<TD><CODE><B><A HREF="../../../../../com/google/javascript/rhino/jstype/NoObjectType.html#matchesObjectContext()">matchesObjectContext</A></B>()</CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;This predicate is used to test whether a given type can appear in an
 <code>Object</code> context, such as the expression in a with statement.</TD>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD ALIGN="right" VALIGN="top" WIDTH="1%"><FONT SIZE="-1">
<CODE>&nbsp;boolean</CODE></FONT></TD>
<TD><CODE><B><A HREF="../../../../../com/google/javascript/rhino/jstype/NoObjectType.html#matchesStringContext()">matchesStringContext</A></B>()</CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;This predicate is used to test whether a given type can appear in a
 <code>String</code> context, such as an operand of a string concat (+) operator.</TD>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD ALIGN="right" VALIGN="top" WIDTH="1%"><FONT SIZE="-1">
<CODE>&nbsp;void</CODE></FONT></TD>
<TD><CODE><B><A HREF="../../../../../com/google/javascript/rhino/jstype/NoObjectType.html#setPropertyJSDocInfo(java.lang.String, com.google.javascript.rhino.JSDocInfo, boolean)">setPropertyJSDocInfo</A></B>(<A HREF="http://java.sun.com/javase/6/docs/api/java/lang/String.html?is-external=true" title="class or interface in java.lang">String</A>&nbsp;propertyName,
                     <A HREF="../../../../../com/google/javascript/rhino/JSDocInfo.html" title="class in com.google.javascript.rhino">JSDocInfo</A>&nbsp;info,
                     boolean&nbsp;inExterns)</CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Sets the docInfo for the specified property from the
 <A HREF="../../../../../com/google/javascript/rhino/JSDocInfo.html" title="class in com.google.javascript.rhino"><CODE>JSDocInfo</CODE></A> on its definition.</TD>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD ALIGN="right" VALIGN="top" WIDTH="1%"><FONT SIZE="-1">
<CODE>&nbsp;<A HREF="../../../../../com/google/javascript/rhino/jstype/TernaryValue.html" title="enum in com.google.javascript.rhino.jstype">TernaryValue</A></CODE></FONT></TD>
<TD><CODE><B><A HREF="../../../../../com/google/javascript/rhino/jstype/NoObjectType.html#testForEquality(com.google.javascript.rhino.jstype.JSType)">testForEquality</A></B>(<A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html" title="class in com.google.javascript.rhino.jstype">JSType</A>&nbsp;that)</CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Compares <code>this</code> and <code>that</code>.</TD>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD ALIGN="right" VALIGN="top" WIDTH="1%"><FONT SIZE="-1">
<CODE>&nbsp;<A HREF="http://java.sun.com/javase/6/docs/api/java/lang/String.html?is-external=true" title="class or interface in java.lang">String</A></CODE></FONT></TD>
<TD><CODE><B><A HREF="../../../../../com/google/javascript/rhino/jstype/NoObjectType.html#toString()">toString</A></B>()</CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Informally, a function is represented by
 <code>function (params): returnType</code> where the <code>params</code> is a comma
 separated list of types, the first one being a special
 <code>this:T</code> if the function expects a known type for <code>this</code>.</TD>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD ALIGN="right" VALIGN="top" WIDTH="1%"><FONT SIZE="-1">
<CODE>&nbsp;<A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html" title="class in com.google.javascript.rhino.jstype">JSType</A></CODE></FONT></TD>
<TD><CODE><B><A HREF="../../../../../com/google/javascript/rhino/jstype/NoObjectType.html#unboxesTo()">unboxesTo</A></B>()</CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Gets the type to which this type unboxes.</TD>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD ALIGN="right" VALIGN="top" WIDTH="1%"><FONT SIZE="-1">
<CODE>
<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="0" SUMMARY="">
<TR ALIGN="right" VALIGN="">
<TD NOWRAP><FONT SIZE="-1">
<CODE>&lt;T&gt; T</CODE></FONT></TD>
</TR>
</TABLE>
</CODE></FONT></TD>
<TD><CODE><B><A HREF="../../../../../com/google/javascript/rhino/jstype/NoObjectType.html#visit(com.google.javascript.rhino.jstype.Visitor)">visit</A></B>(<A HREF="../../../../../com/google/javascript/rhino/jstype/Visitor.html" title="interface in com.google.javascript.rhino.jstype">Visitor</A>&lt;T&gt;&nbsp;visitor)</CODE>

<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Visit this type with the given visitor.</TD>
</TR>
</TABLE>
&nbsp;<A NAME="methods_inherited_from_class_com.google.javascript.rhino.jstype.FunctionType"><!-- --></A>
<TABLE BORDER="1" WIDTH="100%" CELLPADDING="3" CELLSPACING="0" SUMMARY="">
<TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
<TH ALIGN="left"><B>Methods inherited from class com.google.javascript.rhino.jstype.<A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html" title="class in com.google.javascript.rhino.jstype">FunctionType</A></B></TH>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD><CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#canBeCalled()">canBeCalled</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#getAllImplementedInterfaces()">getAllImplementedInterfaces</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#getImplementedInterfaces()">getImplementedInterfaces</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#getMaxArguments()">getMaxArguments</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#getMinArguments()">getMinArguments</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#getParameters()">getParameters</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#getParametersNode()">getParametersNode</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#getPrototype()">getPrototype</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#getSource()">getSource</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#getSubTypes()">getSubTypes</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#getSuperClassConstructor()">getSuperClassConstructor</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#getTemplateTypeName()">getTemplateTypeName</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#getTopMostDefiningType(java.lang.String)">getTopMostDefiningType</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#getTypeOfThis()">getTypeOfThis</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#hasCachedValues()">hasCachedValues</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#hasEqualCallType(com.google.javascript.rhino.jstype.FunctionType)">hasEqualCallType</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#hasInstanceType()">hasInstanceType</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#hasUnknownSupertype()">hasUnknownSupertype</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#isConstructor()">isConstructor</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#isInstanceType()">isInstanceType</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#isInterface()">isInterface</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#isOrdinaryFunction()">isOrdinaryFunction</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#setImplementedInterfaces(java.util.List)">setImplementedInterfaces</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#setPrototype(com.google.javascript.rhino.jstype.FunctionPrototypeType)">setPrototype</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#setPrototypeBasedOn(com.google.javascript.rhino.jstype.ObjectType)">setPrototypeBasedOn</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#setSource(com.google.javascript.rhino.Node)">setSource</A></CODE></TD>
</TR>
</TABLE>
&nbsp;<A NAME="methods_inherited_from_class_com.google.javascript.rhino.jstype.ObjectType"><!-- --></A>
<TABLE BORDER="1" WIDTH="100%" CELLPADDING="3" CELLSPACING="0" SUMMARY="">
<TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
<TH ALIGN="left"><B>Methods inherited from class com.google.javascript.rhino.jstype.<A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html" title="class in com.google.javascript.rhino.jstype">ObjectType</A></B></TH>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD><CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html#defineDeclaredProperty(java.lang.String, com.google.javascript.rhino.jstype.JSType, boolean)">defineDeclaredProperty</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html#defineInferredProperty(java.lang.String, com.google.javascript.rhino.jstype.JSType, boolean)">defineInferredProperty</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html#findPropertyType(java.lang.String)">findPropertyType</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html#getIndexType()">getIndexType</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html#getJSDocInfo()">getJSDocInfo</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html#getParameterType()">getParameterType</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html#getPossibleToBooleanOutcomes()">getPossibleToBooleanOutcomes</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html#getPropertyNames()">getPropertyNames</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html#isObject()">isObject</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html#isUnknownType()">isUnknownType</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html#setJSDocInfo(com.google.javascript.rhino.JSDocInfo)">setJSDocInfo</A></CODE></TD>
</TR>
</TABLE>
&nbsp;<A NAME="methods_inherited_from_class_com.google.javascript.rhino.jstype.JSType"><!-- --></A>
<TABLE BORDER="1" WIDTH="100%" CELLPADDING="3" CELLSPACING="0" SUMMARY="">
<TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
<TH ALIGN="left"><B>Methods inherited from class com.google.javascript.rhino.jstype.<A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html" title="class in com.google.javascript.rhino.jstype">JSType</A></B></TH>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD><CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#autoboxesTo()">autoboxesTo</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#canAssignTo(com.google.javascript.rhino.jstype.JSType)">canAssignTo</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#canTestForEqualityWith(com.google.javascript.rhino.jstype.JSType)">canTestForEqualityWith</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#canTestForShallowEqualityWith(com.google.javascript.rhino.jstype.JSType)">canTestForShallowEqualityWith</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#dereference()">dereference</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#differsFrom(com.google.javascript.rhino.jstype.JSType)">differsFrom</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#forgiveUnknownNames()">forgiveUnknownNames</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#getRestrictedTypeGivenToBooleanOutcome(boolean)">getRestrictedTypeGivenToBooleanOutcome</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#getTypesUnderEquality(com.google.javascript.rhino.jstype.JSType)">getTypesUnderEquality</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#getTypesUnderInequality(com.google.javascript.rhino.jstype.JSType)">getTypesUnderInequality</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#getTypesUnderShallowEquality(com.google.javascript.rhino.jstype.JSType)">getTypesUnderShallowEquality</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#getTypesUnderShallowInequality(com.google.javascript.rhino.jstype.JSType)">getTypesUnderShallowInequality</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#isAllType()">isAllType</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#isArrayType()">isArrayType</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#isBooleanObjectType()">isBooleanObjectType</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#isBooleanValueType()">isBooleanValueType</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#isCheckedUnknownType()">isCheckedUnknownType</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#isDateType()">isDateType</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#isEmptyType()">isEmptyType</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#isEnumElementType()">isEnumElementType</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#isEnumType()">isEnumType</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#isFunctionPrototypeType()">isFunctionPrototypeType</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#isNamedType()">isNamedType</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#isNominalType()">isNominalType</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#isNoType()">isNoType</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#isNullable()">isNullable</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#isNullType()">isNullType</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#isNumber()">isNumber</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#isNumberObjectType()">isNumberObjectType</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#isNumberValueType()">isNumberValueType</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#isRecordType()">isRecordType</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#isRegexpType()">isRegexpType</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#isString()">isString</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#isStringObjectType()">isStringObjectType</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#isStringValueType()">isStringValueType</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#isTemplateType()">isTemplateType</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#isUnionType()">isUnionType</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#isVoidType()">isVoidType</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#matchesInt32Context()">matchesInt32Context</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#matchesUint32Context()">matchesUint32Context</A>, <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#restrictByNotNullOrUndefined()">restrictByNotNullOrUndefined</A></CODE></TD>
</TR>
</TABLE>
&nbsp;<A NAME="methods_inherited_from_class_java.lang.Object"><!-- --></A>
<TABLE BORDER="1" WIDTH="100%" CELLPADDING="3" CELLSPACING="0" SUMMARY="">
<TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
<TH ALIGN="left"><B>Methods inherited from class java.lang.<A HREF="http://java.sun.com/javase/6/docs/api/java/lang/Object.html?is-external=true" title="class or interface in java.lang">Object</A></B></TH>
</TR>
<TR BGCOLOR="white" CLASS="TableRowColor">
<TD><CODE><A HREF="http://java.sun.com/javase/6/docs/api/java/lang/Object.html?is-external=true#clone()" title="class or interface in java.lang">clone</A>, <A HREF="http://java.sun.com/javase/6/docs/api/java/lang/Object.html?is-external=true#finalize()" title="class or interface in java.lang">finalize</A>, <A HREF="http://java.sun.com/javase/6/docs/api/java/lang/Object.html?is-external=true#getClass()" title="class or interface in java.lang">getClass</A>, <A HREF="http://java.sun.com/javase/6/docs/api/java/lang/Object.html?is-external=true#notify()" title="class or interface in java.lang">notify</A>, <A HREF="http://java.sun.com/javase/6/docs/api/java/lang/Object.html?is-external=true#notifyAll()" title="class or interface in java.lang">notifyAll</A>, <A HREF="http://java.sun.com/javase/6/docs/api/java/lang/Object.html?is-external=true#wait()" title="class or interface in java.lang">wait</A>, <A HREF="http://java.sun.com/javase/6/docs/api/java/lang/Object.html?is-external=true#wait(long)" title="class or interface in java.lang">wait</A>, <A HREF="http://java.sun.com/javase/6/docs/api/java/lang/Object.html?is-external=true#wait(long, int)" title="class or interface in java.lang">wait</A></CODE></TD>
</TR>
</TABLE>
&nbsp;
<P>

<!-- ============ METHOD DETAIL ========== -->

<A NAME="method_detail"><!-- --></A>
<TABLE BORDER="1" WIDTH="100%" CELLPADDING="3" CELLSPACING="0" SUMMARY="">
<TR BGCOLOR="#CCCCFF" CLASS="TableHeadingColor">
<TH ALIGN="left" COLSPAN="1"><FONT SIZE="+2">
<B>Method Detail</B></FONT></TH>
</TR>
</TABLE>

<A NAME="getReturnType()"><!-- --></A><H3>
getReturnType</H3>
<PRE>
public <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html" title="class in com.google.javascript.rhino.jstype">JSType</A> <B>getReturnType</B>()</PRE>
<DL>
<DD><DL>
<DT><B>Overrides:</B><DD><CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#getReturnType()">getReturnType</A></CODE> in class <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html" title="class in com.google.javascript.rhino.jstype">FunctionType</A></CODE></DL>
</DD>
<DD><DL>
</DL>
</DD>
</DL>
<HR>

<A NAME="getInstanceType()"><!-- --></A><H3>
getInstanceType</H3>
<PRE>
public <A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html" title="class in com.google.javascript.rhino.jstype">ObjectType</A> <B>getInstanceType</B>()</PRE>
<DL>
<DD><B>Description copied from class: <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#getInstanceType()">FunctionType</A></CODE></B></DD>
<DD>Gets the type of instance of this function.
<P>
<DD><DL>
<DT><B>Overrides:</B><DD><CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#getInstanceType()">getInstanceType</A></CODE> in class <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html" title="class in com.google.javascript.rhino.jstype">FunctionType</A></CODE></DL>
</DD>
<DD><DL>
</DL>
</DD>
</DL>
<HR>

<A NAME="testForEquality(com.google.javascript.rhino.jstype.JSType)"><!-- --></A><H3>
testForEquality</H3>
<PRE>
public <A HREF="../../../../../com/google/javascript/rhino/jstype/TernaryValue.html" title="enum in com.google.javascript.rhino.jstype">TernaryValue</A> <B>testForEquality</B>(<A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html" title="class in com.google.javascript.rhino.jstype">JSType</A>&nbsp;that)</PRE>
<DL>
<DD><B>Description copied from class: <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#testForEquality(com.google.javascript.rhino.jstype.JSType)">JSType</A></CODE></B></DD>
<DD>Compares <code>this</code> and <code>that</code>.
<P>
<DD><DL>
<DT><B>Overrides:</B><DD><CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html#testForEquality(com.google.javascript.rhino.jstype.JSType)">testForEquality</A></CODE> in class <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html" title="class in com.google.javascript.rhino.jstype">ObjectType</A></CODE></DL>
</DD>
<DD><DL>

<DT><B>Returns:</B><DD><ul>
 <li><A HREF="../../../../../com/google/javascript/rhino/jstype/TernaryValue.html#TRUE"><CODE>TernaryValue.TRUE</CODE></A> if the comparison of values of
   <code>this</code> type and <code>that</code> always succeed (such as
   <code>undefined</code> compared to <code>null</code>)</li>
 <li><A HREF="../../../../../com/google/javascript/rhino/jstype/TernaryValue.html#FALSE"><CODE>TernaryValue.FALSE</CODE></A> if the comparison of values of
   <code>this</code> type and <code>that</code> always fails (such as
   <code>undefined</code> compared to <code>number</code>)</li>
 <li><A HREF="../../../../../com/google/javascript/rhino/jstype/TernaryValue.html#UNKNOWN"><CODE>TernaryValue.UNKNOWN</CODE></A> if the comparison can succeed or
   fail depending on the concrete values</li>
 </ul></DL>
</DD>
</DL>
<HR>

<A NAME="isSubtype(com.google.javascript.rhino.jstype.JSType)"><!-- --></A><H3>
isSubtype</H3>
<PRE>
public boolean <B>isSubtype</B>(<A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html" title="class in com.google.javascript.rhino.jstype">JSType</A>&nbsp;that)</PRE>
<DL>
<DD><B>Description copied from class: <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#isSubtype(com.google.javascript.rhino.jstype.JSType)">FunctionType</A></CODE></B></DD>
<DD>A function is a subtype of another if their call methods are related via
 subtyping and <code>this</code> is a subtype of <code>that</code> with regard to
 the prototype chain.
<P>
<DD><DL>
<DT><B>Overrides:</B><DD><CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#isSubtype(com.google.javascript.rhino.jstype.JSType)">isSubtype</A></CODE> in class <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html" title="class in com.google.javascript.rhino.jstype">FunctionType</A></CODE></DL>
</DD>
<DD><DL>

<DT><B>Returns:</B><DD><code>this &amp;lt;: that</code></DL>
</DD>
</DL>
<HR>

<A NAME="isFunctionType()"><!-- --></A><H3>
isFunctionType</H3>
<PRE>
public boolean <B>isFunctionType</B>()</PRE>
<DL>
<DD><DL>
<DT><B>Overrides:</B><DD><CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#isFunctionType()">isFunctionType</A></CODE> in class <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html" title="class in com.google.javascript.rhino.jstype">FunctionType</A></CODE></DL>
</DD>
<DD><DL>
</DL>
</DD>
</DL>
<HR>

<A NAME="isNoObjectType()"><!-- --></A><H3>
isNoObjectType</H3>
<PRE>
public boolean <B>isNoObjectType</B>()</PRE>
<DL>
<DD><DL>
<DT><B>Overrides:</B><DD><CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#isNoObjectType()">isNoObjectType</A></CODE> in class <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html" title="class in com.google.javascript.rhino.jstype">JSType</A></CODE></DL>
</DD>
<DD><DL>
</DL>
</DD>
</DL>
<HR>

<A NAME="getLeastSupertype(com.google.javascript.rhino.jstype.JSType)"><!-- --></A><H3>
getLeastSupertype</H3>
<PRE>
public <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html" title="class in com.google.javascript.rhino.jstype">JSType</A> <B>getLeastSupertype</B>(<A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html" title="class in com.google.javascript.rhino.jstype">JSType</A>&nbsp;that)</PRE>
<DL>
<DD><B>Description copied from class: <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#getLeastSupertype(com.google.javascript.rhino.jstype.JSType)">JSType</A></CODE></B></DD>
<DD>Gets the least supertype of <code>this</code> and <code>that</code>.
 The least supertype is the join (&#8744;) or supremum of both types in the
 type lattice.<p>
 Examples:
 <ul>
 <li><code>number &amp;#8744; *</code> = <code>*</code></li>
 <li><code>number &amp;#8744; Object</code> = <code>(number, Object)</code></li>
 <li><code>Number &amp;#8744; Object</code> = <code>Object</code></li>
 </ul>
<P>
<DD><DL>
<DT><B>Overrides:</B><DD><CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#getLeastSupertype(com.google.javascript.rhino.jstype.JSType)">getLeastSupertype</A></CODE> in class <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html" title="class in com.google.javascript.rhino.jstype">FunctionType</A></CODE></DL>
</DD>
<DD><DL>

<DT><B>Returns:</B><DD><code>this &amp;#8744; that</code></DL>
</DD>
</DL>
<HR>

<A NAME="getGreatestSubtype(com.google.javascript.rhino.jstype.JSType)"><!-- --></A><H3>
getGreatestSubtype</H3>
<PRE>
public <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html" title="class in com.google.javascript.rhino.jstype">JSType</A> <B>getGreatestSubtype</B>(<A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html" title="class in com.google.javascript.rhino.jstype">JSType</A>&nbsp;that)</PRE>
<DL>
<DD><B>Description copied from class: <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#getGreatestSubtype(com.google.javascript.rhino.jstype.JSType)">JSType</A></CODE></B></DD>
<DD>Gets the greatest subtype of <code>this</code> and <code>that</code>.
 The greatest subtype is the meet (&#8743;) or infimum of both types in the
 type lattice.<p>
 Examples
 <ul>
 <li><code>Number &amp;#8743; Any</code> = <code>Any</code></li>
 <li><code>number &amp;#8743; Object</code> = <code>Any</code></li>
 <li><code>Number &amp;#8743; Object</code> = <code>Number</code></li>
 </ul>
<P>
<DD><DL>
<DT><B>Overrides:</B><DD><CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#getGreatestSubtype(com.google.javascript.rhino.jstype.JSType)">getGreatestSubtype</A></CODE> in class <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html" title="class in com.google.javascript.rhino.jstype">FunctionType</A></CODE></DL>
</DD>
<DD><DL>

<DT><B>Returns:</B><DD><code>this &amp;#8744; that</code></DL>
</DD>
</DL>
<HR>

<A NAME="getImplicitPrototype()"><!-- --></A><H3>
getImplicitPrototype</H3>
<PRE>
public <A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html" title="class in com.google.javascript.rhino.jstype">ObjectType</A> <B>getImplicitPrototype</B>()</PRE>
<DL>
<DD><B>Description copied from class: <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html#getImplicitPrototype()">ObjectType</A></CODE></B></DD>
<DD>Gets the implicit prototype (a.k.a. the <code>[[Prototype]]</code> property).
<P>
<DD><DL>
</DL>
</DD>
<DD><DL>
</DL>
</DD>
</DL>
<HR>

<A NAME="getReferenceName()"><!-- --></A><H3>
getReferenceName</H3>
<PRE>
public <A HREF="http://java.sun.com/javase/6/docs/api/java/lang/String.html?is-external=true" title="class or interface in java.lang">String</A> <B>getReferenceName</B>()</PRE>
<DL>
<DD><B>Description copied from class: <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html#getReferenceName()">ObjectType</A></CODE></B></DD>
<DD>Gets the reference name for this object. This includes named types
 like constructors, prototypes, and enums. It notably does not include
 literal types like strings and booleans and structural types.
<P>
<DD><DL>
</DL>
</DD>
<DD><DL>

<DT><B>Returns:</B><DD>the object's name or <code>null</code> if this is an anonymous
         object</DL>
</DD>
</DL>
<HR>

<A NAME="matchesNumberContext()"><!-- --></A><H3>
matchesNumberContext</H3>
<PRE>
public boolean <B>matchesNumberContext</B>()</PRE>
<DL>
<DD><B>Description copied from class: <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#matchesNumberContext()">JSType</A></CODE></B></DD>
<DD>This predicate is used to test whether a given type can appear in a
 numeric context, such as an operand of a multiply operator.
<P>
<DD><DL>
</DL>
</DD>
<DD><DL>
</DL>
</DD>
</DL>
<HR>

<A NAME="matchesObjectContext()"><!-- --></A><H3>
matchesObjectContext</H3>
<PRE>
public boolean <B>matchesObjectContext</B>()</PRE>
<DL>
<DD><B>Description copied from class: <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#matchesObjectContext()">JSType</A></CODE></B></DD>
<DD>This predicate is used to test whether a given type can appear in an
 <code>Object</code> context, such as the expression in a with statement.

 Most types we will encounter, except notably <code>null</code>, have at least
 the potential for converting to <code>Object</code>.  Host defined objects can
 get peculiar.
<P>
<DD><DL>
</DL>
</DD>
<DD><DL>
</DL>
</DD>
</DL>
<HR>

<A NAME="matchesStringContext()"><!-- --></A><H3>
matchesStringContext</H3>
<PRE>
public boolean <B>matchesStringContext</B>()</PRE>
<DL>
<DD><B>Description copied from class: <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#matchesStringContext()">JSType</A></CODE></B></DD>
<DD>This predicate is used to test whether a given type can appear in a
 <code>String</code> context, such as an operand of a string concat (+) operator.

 All types have at least the potential for converting to <code>String</code>.
 When we add externally defined types, such as a browser OM, we may choose
 to add types that do not automatically convert to <code>String</code>.
<P>
<DD><DL>
</DL>
</DD>
<DD><DL>
</DL>
</DD>
</DL>
<HR>

<A NAME="equals(java.lang.Object)"><!-- --></A><H3>
equals</H3>
<PRE>
public boolean <B>equals</B>(<A HREF="http://java.sun.com/javase/6/docs/api/java/lang/Object.html?is-external=true" title="class or interface in java.lang">Object</A>&nbsp;that)</PRE>
<DL>
<DD><B>Description copied from class: <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#equals(java.lang.Object)">FunctionType</A></CODE></B></DD>
<DD>Two function types are equal if their signatures match. Since they don't
 have signatures, two interfaces are equal if their names match.
<P>
<DD><DL>
<DT><B>Overrides:</B><DD><CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#equals(java.lang.Object)">equals</A></CODE> in class <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html" title="class in com.google.javascript.rhino.jstype">FunctionType</A></CODE></DL>
</DD>
<DD><DL>
</DL>
</DD>
</DL>
<HR>

<A NAME="hashCode()"><!-- --></A><H3>
hashCode</H3>
<PRE>
public int <B>hashCode</B>()</PRE>
<DL>
<DD><DL>
<DT><B>Overrides:</B><DD><CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#hashCode()">hashCode</A></CODE> in class <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html" title="class in com.google.javascript.rhino.jstype">FunctionType</A></CODE></DL>
</DD>
<DD><DL>
</DL>
</DD>
</DL>
<HR>

<A NAME="getPropertiesCount()"><!-- --></A><H3>
getPropertiesCount</H3>
<PRE>
public int <B>getPropertiesCount</B>()</PRE>
<DL>
<DD>Gets the number of properties of this object.
<P>
<DD><DL>
</DL>
</DD>
<DD><DL>
</DL>
</DD>
</DL>
<HR>

<A NAME="getPropertyType(java.lang.String)"><!-- --></A><H3>
getPropertyType</H3>
<PRE>
public <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html" title="class in com.google.javascript.rhino.jstype">JSType</A> <B>getPropertyType</B>(<A HREF="http://java.sun.com/javase/6/docs/api/java/lang/String.html?is-external=true" title="class or interface in java.lang">String</A>&nbsp;propertyName)</PRE>
<DL>
<DD><B>Description copied from class: <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html#getPropertyType(java.lang.String)">ObjectType</A></CODE></B></DD>
<DD>Gets the property type of the property whose name is given. If the
 underlying object does not have this property, the Unknown type is
 returned to indicate that no information is available on this property.
<P>
<DD><DL>
<DT><B>Overrides:</B><DD><CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#getPropertyType(java.lang.String)">getPropertyType</A></CODE> in class <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html" title="class in com.google.javascript.rhino.jstype">FunctionType</A></CODE></DL>
</DD>
<DD><DL>

<DT><B>Returns:</B><DD>the property's type or <A HREF="../../../../../com/google/javascript/rhino/jstype/UnknownType.html" title="class in com.google.javascript.rhino.jstype"><CODE>UnknownType</CODE></A>. This method never
         returns <code>null</code>.</DL>
</DD>
</DL>
<HR>

<A NAME="hasProperty(java.lang.String)"><!-- --></A><H3>
hasProperty</H3>
<PRE>
public boolean <B>hasProperty</B>(<A HREF="http://java.sun.com/javase/6/docs/api/java/lang/String.html?is-external=true" title="class or interface in java.lang">String</A>&nbsp;propertyName)</PRE>
<DL>
<DD><B>Description copied from class: <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html#hasProperty(java.lang.String)">ObjectType</A></CODE></B></DD>
<DD>Checks whether the property whose name is given is present on the
 object.
<P>
<DD><DL>
<DT><B>Overrides:</B><DD><CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#hasProperty(java.lang.String)">hasProperty</A></CODE> in class <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html" title="class in com.google.javascript.rhino.jstype">FunctionType</A></CODE></DL>
</DD>
<DD><DL>
</DL>
</DD>
</DL>
<HR>

<A NAME="getOwnPropertyJSDocInfo(java.lang.String)"><!-- --></A><H3>
getOwnPropertyJSDocInfo</H3>
<PRE>
public <A HREF="../../../../../com/google/javascript/rhino/JSDocInfo.html" title="class in com.google.javascript.rhino">JSDocInfo</A> <B>getOwnPropertyJSDocInfo</B>(<A HREF="http://java.sun.com/javase/6/docs/api/java/lang/String.html?is-external=true" title="class or interface in java.lang">String</A>&nbsp;propertyName)</PRE>
<DL>
<DD><B>Description copied from class: <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html#getOwnPropertyJSDocInfo(java.lang.String)">ObjectType</A></CODE></B></DD>
<DD>Gets the docInfo on the specified property on this type.  This should not
 be done implemented recursively, as you generally need to know exactly on
 which type in the prototype chain the JSDocInfo exists.
<P>
<DD><DL>
</DL>
</DD>
<DD><DL>
</DL>
</DD>
</DL>
<HR>

<A NAME="setPropertyJSDocInfo(java.lang.String, com.google.javascript.rhino.JSDocInfo, boolean)"><!-- --></A><H3>
setPropertyJSDocInfo</H3>
<PRE>
public void <B>setPropertyJSDocInfo</B>(<A HREF="http://java.sun.com/javase/6/docs/api/java/lang/String.html?is-external=true" title="class or interface in java.lang">String</A>&nbsp;propertyName,
                                 <A HREF="../../../../../com/google/javascript/rhino/JSDocInfo.html" title="class in com.google.javascript.rhino">JSDocInfo</A>&nbsp;info,
                                 boolean&nbsp;inExterns)</PRE>
<DL>
<DD><B>Description copied from class: <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html#setPropertyJSDocInfo(java.lang.String, com.google.javascript.rhino.JSDocInfo, boolean)">ObjectType</A></CODE></B></DD>
<DD>Sets the docInfo for the specified property from the
 <A HREF="../../../../../com/google/javascript/rhino/JSDocInfo.html" title="class in com.google.javascript.rhino"><CODE>JSDocInfo</CODE></A> on its definition.
<P>
<DD><DL>
</DL>
</DD>
<DD><DL>
<DD><CODE>info</CODE> - <code>JSDocInfo</code> for the property definition. May be
        <code>null</code>.<DD><CODE>inExterns</CODE> - <code>true</code> if this property was defined in an externs
        file. TightenTypes assumes that any function passed to an externs
        property could be called, so setting this incorrectly could result
        in live code being removed.</DL>
</DD>
</DL>
<HR>

<A NAME="isPropertyTypeInferred(java.lang.String)"><!-- --></A><H3>
isPropertyTypeInferred</H3>
<PRE>
public boolean <B>isPropertyTypeInferred</B>(<A HREF="http://java.sun.com/javase/6/docs/api/java/lang/String.html?is-external=true" title="class or interface in java.lang">String</A>&nbsp;propertyName)</PRE>
<DL>
<DD><B>Description copied from class: <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html#isPropertyTypeInferred(java.lang.String)">ObjectType</A></CODE></B></DD>
<DD>Checks whether the property's type is inferred.
<P>
<DD><DL>
<DT><B>Overrides:</B><DD><CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#isPropertyTypeInferred(java.lang.String)">isPropertyTypeInferred</A></CODE> in class <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html" title="class in com.google.javascript.rhino.jstype">FunctionType</A></CODE></DL>
</DD>
<DD><DL>
</DL>
</DD>
</DL>
<HR>

<A NAME="visit(com.google.javascript.rhino.jstype.Visitor)"><!-- --></A><H3>
visit</H3>
<PRE>
public &lt;T&gt; T <B>visit</B>(<A HREF="../../../../../com/google/javascript/rhino/jstype/Visitor.html" title="interface in com.google.javascript.rhino.jstype">Visitor</A>&lt;T&gt;&nbsp;visitor)</PRE>
<DL>
<DD><B>Description copied from class: <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#visit(com.google.javascript.rhino.jstype.Visitor)">JSType</A></CODE></B></DD>
<DD>Visit this type with the given visitor.
<P>
<DD><DL>
<DT><B>Overrides:</B><DD><CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#visit(com.google.javascript.rhino.jstype.Visitor)">visit</A></CODE> in class <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html" title="class in com.google.javascript.rhino.jstype">FunctionType</A></CODE></DL>
</DD>
<DD><DL>

<DT><B>Returns:</B><DD>the value returned by the visitor<DT><B>See Also:</B><DD><A HREF="../../../../../com/google/javascript/rhino/jstype/Visitor.html" title="interface in com.google.javascript.rhino.jstype"><CODE>Visitor</CODE></A></DL>
</DD>
</DL>
<HR>

<A NAME="toString()"><!-- --></A><H3>
toString</H3>
<PRE>
public <A HREF="http://java.sun.com/javase/6/docs/api/java/lang/String.html?is-external=true" title="class or interface in java.lang">String</A> <B>toString</B>()</PRE>
<DL>
<DD><B>Description copied from class: <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#toString()">FunctionType</A></CODE></B></DD>
<DD>Informally, a function is represented by
 <code>function (params): returnType</code> where the <code>params</code> is a comma
 separated list of types, the first one being a special
 <code>this:T</code> if the function expects a known type for <code>this</code>.
<P>
<DD><DL>
<DT><B>Overrides:</B><DD><CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html#toString()">toString</A></CODE> in class <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html" title="class in com.google.javascript.rhino.jstype">FunctionType</A></CODE></DL>
</DD>
<DD><DL>
</DL>
</DD>
</DL>
<HR>

<A NAME="getConstructor()"><!-- --></A><H3>
getConstructor</H3>
<PRE>
public <A HREF="../../../../../com/google/javascript/rhino/jstype/FunctionType.html" title="class in com.google.javascript.rhino.jstype">FunctionType</A> <B>getConstructor</B>()</PRE>
<DL>
<DD><B>Description copied from class: <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html#getConstructor()">ObjectType</A></CODE></B></DD>
<DD>Gets this object's constructor.
<P>
<DD><DL>
</DL>
</DD>
<DD><DL>

<DT><B>Returns:</B><DD>this object's constructor or <code>null</code> if it is a native
 object (constructed natively v.s. by instantiation of a function)</DL>
</DD>
</DL>
<HR>

<A NAME="hasOwnProperty(java.lang.String)"><!-- --></A><H3>
hasOwnProperty</H3>
<PRE>
public boolean <B>hasOwnProperty</B>(<A HREF="http://java.sun.com/javase/6/docs/api/java/lang/String.html?is-external=true" title="class or interface in java.lang">String</A>&nbsp;propertyName)</PRE>
<DL>
<DD><B>Description copied from class: <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html#hasOwnProperty(java.lang.String)">ObjectType</A></CODE></B></DD>
<DD>Checks whether the property whose name is given is present directly on
 the object.  Returns false even if it is declared on a supertype.
<P>
<DD><DL>
<DT><B>Overrides:</B><DD><CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html#hasOwnProperty(java.lang.String)">hasOwnProperty</A></CODE> in class <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html" title="class in com.google.javascript.rhino.jstype">ObjectType</A></CODE></DL>
</DD>
<DD><DL>
</DL>
</DD>
</DL>
<HR>

<A NAME="getOwnPropertyNames()"><!-- --></A><H3>
getOwnPropertyNames</H3>
<PRE>
public <A HREF="http://java.sun.com/javase/6/docs/api/java/util/Set.html?is-external=true" title="class or interface in java.util">Set</A>&lt;<A HREF="http://java.sun.com/javase/6/docs/api/java/lang/String.html?is-external=true" title="class or interface in java.lang">String</A>&gt; <B>getOwnPropertyNames</B>()</PRE>
<DL>
<DD><B>Description copied from class: <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html#getOwnPropertyNames()">ObjectType</A></CODE></B></DD>
<DD>Returns the names of all the properties directly on this type.
<P>
<DD><DL>
<DT><B>Overrides:</B><DD><CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html#getOwnPropertyNames()">getOwnPropertyNames</A></CODE> in class <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html" title="class in com.google.javascript.rhino.jstype">ObjectType</A></CODE></DL>
</DD>
<DD><DL>
</DL>
</DD>
</DL>
<HR>

<A NAME="isPropertyTypeDeclared(java.lang.String)"><!-- --></A><H3>
isPropertyTypeDeclared</H3>
<PRE>
public boolean <B>isPropertyTypeDeclared</B>(<A HREF="http://java.sun.com/javase/6/docs/api/java/lang/String.html?is-external=true" title="class or interface in java.lang">String</A>&nbsp;property)</PRE>
<DL>
<DD><B>Description copied from class: <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html#isPropertyTypeDeclared(java.lang.String)">ObjectType</A></CODE></B></DD>
<DD>Checks whether the property's type is declared.
<P>
<DD><DL>
<DT><B>Specified by:</B><DD><CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html#isPropertyTypeDeclared(java.lang.String)">isPropertyTypeDeclared</A></CODE> in class <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html" title="class in com.google.javascript.rhino.jstype">ObjectType</A></CODE></DL>
</DD>
<DD><DL>
</DL>
</DD>
</DL>
<HR>

<A NAME="collectPropertyNames(java.util.Set)"><!-- --></A><H3>
collectPropertyNames</H3>
<PRE>
protected void <B>collectPropertyNames</B>(<A HREF="http://java.sun.com/javase/6/docs/api/java/util/Set.html?is-external=true" title="class or interface in java.util">Set</A>&lt;<A HREF="http://java.sun.com/javase/6/docs/api/java/lang/String.html?is-external=true" title="class or interface in java.lang">String</A>&gt;&nbsp;props)</PRE>
<DL>
<DD><B>Description copied from class: <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html#collectPropertyNames(java.util.Set)">ObjectType</A></CODE></B></DD>
<DD>Adds any properties defined on this type or its supertypes to the set.
<P>
<DD><DL>
</DL>
</DD>
<DD><DL>
</DL>
</DD>
</DL>
<HR>

<A NAME="isPropertyInExterns(java.lang.String)"><!-- --></A><H3>
isPropertyInExterns</H3>
<PRE>
public boolean <B>isPropertyInExterns</B>(<A HREF="http://java.sun.com/javase/6/docs/api/java/lang/String.html?is-external=true" title="class or interface in java.lang">String</A>&nbsp;propertyName)</PRE>
<DL>
<DD><B>Description copied from class: <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html#isPropertyInExterns(java.lang.String)">ObjectType</A></CODE></B></DD>
<DD>Checks whether the property was defined in the externs.
<P>
<DD><DL>
<DT><B>Overrides:</B><DD><CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html#isPropertyInExterns(java.lang.String)">isPropertyInExterns</A></CODE> in class <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html" title="class in com.google.javascript.rhino.jstype">ObjectType</A></CODE></DL>
</DD>
<DD><DL>
</DL>
</DD>
</DL>
<HR>

<A NAME="unboxesTo()"><!-- --></A><H3>
unboxesTo</H3>
<PRE>
public <A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html" title="class in com.google.javascript.rhino.jstype">JSType</A> <B>unboxesTo</B>()</PRE>
<DL>
<DD><B>Description copied from class: <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#unboxesTo()">JSType</A></CODE></B></DD>
<DD>Gets the type to which this type unboxes.
<P>
<DD><DL>
<DT><B>Overrides:</B><DD><CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html#unboxesTo()">unboxesTo</A></CODE> in class <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/JSType.html" title="class in com.google.javascript.rhino.jstype">JSType</A></CODE></DL>
</DD>
<DD><DL>

<DT><B>Returns:</B><DD>the unboxed type or <code>null</code> if this type does not unbox.</DL>
</DD>
</DL>
<HR>

<A NAME="hasReferenceName()"><!-- --></A><H3>
hasReferenceName</H3>
<PRE>
public boolean <B>hasReferenceName</B>()</PRE>
<DL>
<DD><B>Description copied from class: <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html#hasReferenceName()">ObjectType</A></CODE></B></DD>
<DD>Returns true if the object is named.
<P>
<DD><DL>
<DT><B>Overrides:</B><DD><CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html#hasReferenceName()">hasReferenceName</A></CODE> in class <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html" title="class in com.google.javascript.rhino.jstype">ObjectType</A></CODE></DL>
</DD>
<DD><DL>

<DT><B>Returns:</B><DD>true if the object is named, false if it is anonymous</DL>
</DD>
</DL>
<HR>

<A NAME="isNativeObjectType()"><!-- --></A><H3>
isNativeObjectType</H3>
<PRE>
public boolean <B>isNativeObjectType</B>()</PRE>
<DL>
<DD>Whether this is a built-in object.
<P>
<DD><DL>
<DT><B>Overrides:</B><DD><CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html#isNativeObjectType()">isNativeObjectType</A></CODE> in class <CODE><A HREF="../../../../../com/google/javascript/rhino/jstype/ObjectType.html" title="class in com.google.javascript.rhino.jstype">ObjectType</A></CODE></DL>
</DD>
<DD><DL>
</DL>
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
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <A HREF="../../../../../overview-summary.html"><FONT CLASS="NavBarFont1"><B>Overview</B></FONT></A>&nbsp;</TD>
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <A HREF="package-summary.html"><FONT CLASS="NavBarFont1"><B>Package</B></FONT></A>&nbsp;</TD>
  <TD BGCOLOR="#FFFFFF" CLASS="NavBarCell1Rev"> &nbsp;<FONT CLASS="NavBarFont1Rev"><B>Class</B></FONT>&nbsp;</TD>
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <A HREF="package-tree.html"><FONT CLASS="NavBarFont1"><B>Tree</B></FONT></A>&nbsp;</TD>
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <A HREF="../../../../../deprecated-list.html"><FONT CLASS="NavBarFont1"><B>Deprecated</B></FONT></A>&nbsp;</TD>
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <A HREF="../../../../../index-all.html"><FONT CLASS="NavBarFont1"><B>Index</B></FONT></A>&nbsp;</TD>
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <A HREF="../../../../../help-doc.html"><FONT CLASS="NavBarFont1"><B>Help</B></FONT></A>&nbsp;</TD>
  </TR>
</TABLE>
</TD>
<TD ALIGN="right" VALIGN="top" ROWSPAN=3><EM>
</EM>
</TD>
</TR>

<TR>
<TD BGCOLOR="white" CLASS="NavBarCell2"><FONT SIZE="-2">
&nbsp;<A HREF="../../../../../com/google/javascript/rhino/jstype/NamedType.html" title="class in com.google.javascript.rhino.jstype"><B>PREV CLASS</B></A>&nbsp;
&nbsp;<A HREF="../../../../../com/google/javascript/rhino/jstype/NoType.html" title="class in com.google.javascript.rhino.jstype"><B>NEXT CLASS</B></A></FONT></TD>
<TD BGCOLOR="white" CLASS="NavBarCell2"><FONT SIZE="-2">
  <A HREF="../../../../../index.html?com/google/javascript/rhino/jstype/NoObjectType.html" target="_top"><B>FRAMES</B></A>  &nbsp;
&nbsp;<A HREF="NoObjectType.html" target="_top"><B>NO FRAMES</B></A>  &nbsp;
&nbsp;<SCRIPT type="text/javascript">
  <!--
  if(window==top) {
    document.writeln('<A HREF="../../../../../allclasses-noframe.html"><B>All Classes</B></A>');
  }
  //-->
</SCRIPT>
<NOSCRIPT>
  <A HREF="../../../../../allclasses-noframe.html"><B>All Classes</B></A>
</NOSCRIPT>


</FONT></TD>
</TR>
<TR>
<TD VALIGN="top" CLASS="NavBarCell3"><FONT SIZE="-2">
  SUMMARY:&nbsp;NESTED&nbsp;|&nbsp;<A HREF="#fields_inherited_from_class_com.google.javascript.rhino.jstype.JSType">FIELD</A>&nbsp;|&nbsp;CONSTR&nbsp;|&nbsp;<A HREF="#method_summary">METHOD</A></FONT></TD>
<TD VALIGN="top" CLASS="NavBarCell3"><FONT SIZE="-2">
DETAIL:&nbsp;FIELD&nbsp;|&nbsp;CONSTR&nbsp;|&nbsp;<A HREF="#method_detail">METHOD</A></FONT></TD>
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
