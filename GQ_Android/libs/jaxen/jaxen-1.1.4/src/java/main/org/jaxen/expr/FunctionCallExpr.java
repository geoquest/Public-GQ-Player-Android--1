/*
 * $Header$
 * $Revision: 1232 $
 * $Date: 2006-11-08 08:37:59 -0800 (Wed, 08 Nov 2006) $
 *
 * ====================================================================
 *
 * Copyright 2000-2002 bob mcwhirter & James Strachan.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 * 
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 * 
 *   * Neither the name of the Jaxen Project nor the names of its
 *     contributors may be used to endorse or promote products derived 
 *     from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * ====================================================================
 * This software consists of voluntary contributions made by many 
 * individuals on behalf of the Jaxen Project and was originally 
 * created by bob mcwhirter <bob@werken.com> and 
 * James Strachan <jstrachan@apache.org>.  For more information on the 
 * Jaxen Project, please see <http://www.jaxen.org/>.
 * 
 * $Id: FunctionCallExpr.java 1232 2006-11-08 16:37:59Z elharo $
 */


package org.jaxen.expr;

import java.util.List;

/**
 * Represents an XPath function call expression. This is production 16 in the 
 * <a href="http://www.w3.org/TR/xpath#NT-FunctionCall">XPath 1.0 specification</a>:
 * 
 * <<pre>[16] FunctionCall ::= FunctionName '(' ( Argument ( ',' Argument )* )? ')'</pre>
 * 
 */
public interface FunctionCallExpr extends Expr
{
    
    /**
     * Returns the namespace prefix of the function. This is the empty
     * string for XPath's built-in functions. 
     * 
     * @return the namespace prefix of the function
     */
    public String getPrefix();
    
    /**
     * Returns the local name of the function. 
     * 
     * @return the local name of the function
     */
    public String getFunctionName();
    
    /**
     * Add the next argument to the function. 
     * 
     * @param parameter a function argument 
     */
    public void addParameter(Expr parameter);
    
    /**
     * Returns the the ordered list of function arguments.
     * Each member of the list is an <code>Expr</code> object.
     * 
     * @return the ordered list of function arguments
     */
    public List getParameters();
    
}