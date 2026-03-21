package com.example.proyecto1_compi1.Analizadores;

import java_cup.runtime.Symbol;
import com.example.proyecto1_compi1.models.ErrorLexico;
import java.util.ArrayList;
import java.util.List;

%%

%public
%class Lexer2
%unicode
%cup
%line
%column
%ignorecase

%{
    private List<ErrorLexico> errores = new ArrayList<>();
    private StringBuilder stringBuffer = new StringBuilder();

    public List<ErrorLexico> getErrores() {
        return errores;
    }

    private Symbol symbol(int type) {
        return new Symbol(type, yyline + 1, yycolumn + 1);
    }

    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline + 1, yycolumn + 1, value);
    }
%}

%state METADATA
%state STRING

/* ======================== MACROS ======================== */

Digito       = [0-9]
Numero       = {Digito}+("."{Digito}+)?
HexColor     = "#"[0-9A-Fa-f]{6}
ESPACIO   = [ \t\r\n\f]

%%

//estado principal

<YYINITIAL> {

    /******************* METADATOS ***********/
    "###"                                       { yybegin(METADATA); return symbol(sym2.TRIPLE_HASH); }

    /****************ETIQUETAS DE CIERRE*************/
    "</section>"                                { return symbol(sym2.SECTION_CLOSE); }
    "</table>"                                  { return symbol(sym2.TABLE_CLOSE); }
    "</open>"                                   { return symbol(sym2.OPEN_CLOSE); }
    "</drop>"                                   { return symbol(sym2.DROP_CLOSE); }
    "</select>"                                 { return symbol(sym2.SELECT_CLOSE); }
    "</multiple>"                               { return symbol(sym2.MULTIPLE_CLOSE); }
    "</style>"                                  { return symbol(sym2.STYLE_CLOSE); }
    "</content>"                                { return symbol(sym2.CONTENT_CLOSE); }
    "</line>"                                   { return symbol(sym2.LINE_CLOSE); }
    "</element>"                                { return symbol(sym2.ELEMENT_CLOSE); }

    /***************ETIQUETAS DE APERTURA CON ATRIBUTOS*****************/
    "<section="                                 { return symbol(sym2.SECTION_OPEN); }
    "<open="                                    { return symbol(sym2.OPEN_TAG); }
    "<drop="                                    { return symbol(sym2.DROP_TAG); }
    "<select="                                  { return symbol(sym2.SELECT_TAG); }
    "<multiple="                                { return symbol(sym2.MULTIPLE_TAG); }

    /*****************ETIQUETAS DE ESTRUCTURA***********************/
    "<table>"                                   { return symbol(sym2.TABLE_OPEN); }
    "<style>"                                   { return symbol(sym2.STYLE_OPEN); }
    "<content>"                                 { return symbol(sym2.CONTENT_OPEN); }
    "<line>"                                    { return symbol(sym2.LINE_OPEN); }
    "<element>"                                 { return symbol(sym2.ELEMENT_OPEN); }

    /********************ETIQUETAS DE PROPIEDADES DE ESTILO*********************/
    "<color="                                   { return symbol(sym2.COLOR_TAG); }
    "<background" {ESPACIO}+ "color="        { return symbol(sym2.BG_COLOR_TAG); }
    "<font" {ESPACIO}+ "family="             { return symbol(sym2.FONT_TAG); }
    "<text" {ESPACIO}+ "size="               { return symbol(sym2.TEXT_SIZE_TAG); }
    "<border,"                                  { return symbol(sym2.BORDER_TAG); }

    /*****************COLOR= SIN < (dentro de border)**********************/
    "color="                                    { return symbol(sym2.COLOR_ATTR); }

    /*********COLOR HSL: <numero,numero,numero>*******************/
    "<" {ESPACIO}* {Numero} {ESPACIO}* "," {ESPACIO}* {Numero} {ESPACIO}* "," {ESPACIO}* {Numero} {ESPACIO}* ">"  { return symbol(sym2.HSL_COLOR, yytext()); }

    /*******************CIERRE DE ETIQUETAS******************/
    "/>"                                        { return symbol(sym2.SELF_CLOSE); }
    ">"                                         { return symbol(sym2.TAG_CLOSE); }

    /************DELIMITADORES******************/
    ","                                         { return symbol(sym2.COMA); }
    "{"                                         { return symbol(sym2.LLLAVE); }
    "}"                                         { return symbol(sym2.RLLAVE); }
    "("                                         { return symbol(sym2.LPAREN); }
    ")"                                         { return symbol(sym2.RPAREN); }

    /***************COLOR HEXADECIMAL***************/
    {HexColor}                                  { return symbol(sym2.HEX_COLOR, yytext()); }

    /******************COLORES POR NOMBRE******************/
    "RED"                                       { return symbol(sym2.RED); }
    "BLUE"                                      { return symbol(sym2.BLUE); }
    "GREEN"                                     { return symbol(sym2.GREEN); }
    "YELLOW"                                    { return symbol(sym2.YELLOW); }
    "BLACK"                                     { return symbol(sym2.BLACK); }
    "WHITE"                                     { return symbol(sym2.WHITE); }
    "PURPLE"                                    { return symbol(sym2.PURPLE); }
    "SKY"                                       { return symbol(sym2.SKY); }

    /**********************FONT FAMILIES************/
    "MONO"                                      { return symbol(sym2.MONO); }
    "SANS_SERIF"                                { return symbol(sym2.SANS_SERIF); }
    "CURSIVE"                                   { return symbol(sym2.CURSIVE); }

    /**************TIPOS DE BORDE***********/
    "LINE"                                      { return symbol(sym2.LINE_TYPE); }
    "DOTTED"                                    { return symbol(sym2.DOTTED); }
    "DOUBLE"                                    { return symbol(sym2.DOUBLE_TYPE); }

    /*****************ORIENTACION***********/
    "VERTICAL"                                  { return symbol(sym2.VERTICAL); }
    "HORIZONTAL"                                { return symbol(sym2.HORIZONTAL); }

    /************NUMEROS (con negativos para correct: -1)***************/
    "-"? {Numero}                               { return symbol(sym2.NUMERO, yytext()); }

    /*****************STRINGS*****************/
    \"                                          { yybegin(STRING); stringBuffer.setLength(0); }

    /******************ESPACIOS EN BLANCO*************/
    {ESPACIO}+                               { /* ignorar */ }

    /*****************ERROR**********/
    .                                           { errores.add(new ErrorLexico(yytext(), yyline + 1, yycolumn + 1, "Lexico", "Caracter no reconocido: '" + yytext() + "'"));
 }
}

/*******************ESTADO METADATOS*************/
// Se ignora todo el contenido

<METADATA> {
    "###"                                       { yybegin(YYINITIAL); return symbol(sym2.TRIPLE_HASH); }
    [^\#]+                                      { /* ignorar contenido de metadatos */ }
    "#"                                         { /* # suelto dentro de metadatos */ }
}

/****************************ESTADO STRING*********************/

<STRING> {
    \"                                          { yybegin(YYINITIAL); return symbol(sym2.CADENA, stringBuffer.toString()); }
    [^\"\\]+                                    { stringBuffer.append(yytext()); }
    \\\"                                        { stringBuffer.append("\""); }
    \\\\                                        { stringBuffer.append("\\"); }
}