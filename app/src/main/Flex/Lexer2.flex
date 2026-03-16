package com.example.proyecto1_compi1.Analizadores;

import com.example.proyecto1_compi1.models.ErrorLexico;
import java.util.ArrayList;
import java.util.List;
import java_cup.runtime.Symbol;

%%

%public
%unicode
%class Lexer2
%cup
%line
%column
%ignorecase
%type java_cup.runtime.Symbol

%state METADATA
%state STRING

%init{
%init}

/**********************macros****************************************/

ESPACIO      = [ \t\r\n]+
NUMERO       = [0-9]+(\.[0-9]+)?
ID           = [a-zA-Z_][a-zA-Z0-9_]*
HEX_COLOR    = #[0-9a-fA-F]{6}
RGB_COLOR    = \([ \t]*[0-9]{1,3}[ \t]*,[ \t]*[0-9]{1,3}[ \t]*,[ \t]*[0-9]{1,3}[ \t]*\)
HSL_COLOR    = \<[ \t]*[0-9]{1,3}[ \t]*,[ \t]*[0-9]{1,3}[ \t]*,[ \t]*[0-9]{1,3}[ \t]*\>

/****************************codigo de usuario*********************/
%{

private List<ErrorLexico> errores = new ArrayList<>();

public List<ErrorLexico> getErrores() {
    return errores;
}

private StringBuilder yyStringBuf = new StringBuilder();
private int stringStartLine = 0;
private int stringStartCol  = 0;

private Symbol symbol(int type) {
    return new Symbol(type, yyline+1, yycolumn+1);
}

private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline+1, yycolumn+1, value);
}

%}

%%

/**********************ESTADO INICIAL*****************************/

<YYINITIAL> {

    "###"                   { yybegin(METADATA); return symbol(sym2.TRIPLE_HASH); }

    /**** etiquetas de apertura con atributos ****/
    "<section="             { return symbol(sym2.TAG_SECTION_OPEN); }
    "<table>"               { return symbol(sym2.TAG_TABLE_OPEN); }
    "<content>"             { return symbol(sym2.TAG_CONTENT_OPEN); }
    "<style>"               { return symbol(sym2.TAG_STYLE_OPEN); }
    "<line>"                { return symbol(sym2.TAG_LINE_OPEN); }
    "<element>"             { return symbol(sym2.TAG_ELEMENT_OPEN); }

    /**** etiquetas de preguntas con atributos ****/
    "<open="                { return symbol(sym2.TAG_OPEN_Q); }
    "<drop="                { return symbol(sym2.TAG_DROP_Q); }
    "<select="              { return symbol(sym2.TAG_SELECT_Q); }
    "<multiple="            { return symbol(sym2.TAG_MULTIPLE_Q); }

    /**** etiquetas de estilos ****/
    "<color="               { return symbol(sym2.TAG_COLOR); }
    "<background color="    { return symbol(sym2.TAG_BG_COLOR); }
    "<font family="         { return symbol(sym2.TAG_FONT); }
    "<text size="           { return symbol(sym2.TAG_TEXT_SIZE); }
    "<border,"              { return symbol(sym2.TAG_BORDER); }

    /**** etiquetas de cierre ****/
    "</section>"            { return symbol(sym2.TAG_SECTION_CLOSE); }
    "</table>"              { return symbol(sym2.TAG_TABLE_CLOSE); }
    "</content>"            { return symbol(sym2.TAG_CONTENT_CLOSE); }
    "</style>"              { return symbol(sym2.TAG_STYLE_CLOSE); }
    "</line>"               { return symbol(sym2.TAG_LINE_CLOSE); }
    "</element>"            { return symbol(sym2.TAG_ELEMENT_CLOSE); }
    "</open>"               { return symbol(sym2.TAG_OPEN_Q_CLOSE); }
    "</drop>"               { return symbol(sym2.TAG_DROP_Q_CLOSE); }
    "</select>"             { return symbol(sym2.TAG_SELECT_Q_CLOSE); }
    "</multiple>"           { return symbol(sym2.TAG_MULTIPLE_Q_CLOSE); }

    /**** cierre de etiquetas ****/
    "/>"                    { return symbol(sym2.SLASH_GT); }
    ">"                     { return symbol(sym2.GT); }

    /**** orientacion ****/
    "VERTICAL"              { return symbol(sym2.VERTICAL); }
    "HORIZONTAL"            { return symbol(sym2.HORIZONTAL); }

    /**** fuentes ****/
    "MONO"                  { return symbol(sym2.MONO); }
    "SANS_SERIF"            { return symbol(sym2.SANS_SERIF); }
    "CURSIVE"               { return symbol(sym2.CURSIVE); }

    /**** tipos de borde ****/
    "LINE"                  { return symbol(sym2.LINE); }
    "DOTTED"                { return symbol(sym2.DOTTED); }
    "DOUBLE"                { return symbol(sym2.DOUBLE); }

    /**** colores base ****/
    "RED"                   { return symbol(sym2.RED); }
    "BLUE"                  { return symbol(sym2.BLUE); }
    "GREEN"                 { return symbol(sym2.GREEN); }
    "PURPLE"                { return symbol(sym2.PURPLE); }
    "SKY"                   { return symbol(sym2.SKY); }
    "YELLOW"                { return symbol(sym2.YELLOW); }
    "BLACK"                 { return symbol(sym2.BLACK); }
    "WHITE"                 { return symbol(sym2.WHITE); }

    /**** simbolos ****/
    ","                     { return symbol(sym2.COMA); }
    "{"                     { return symbol(sym2.LLLAVE); }
    "}"                     { return symbol(sym2.RLLAVE); }
    "#"                     { return symbol(sym2.HASH); }

    /**** colores ****/
    {HEX_COLOR}             { return symbol(sym2.HEX_COLOR, yytext()); }
    {RGB_COLOR}             { return symbol(sym2.RGB_COLOR, yytext()); }
    {HSL_COLOR}             { return symbol(sym2.HSL_COLOR, yytext()); }

    /**** numeros ****/
    {NUMERO}                { return symbol(sym2.NUMERO, Double.parseDouble(yytext())); }

    /**** cadenas ****/
    \"  {
        stringStartLine = yyline + 1;
        stringStartCol  = yycolumn + 1;
        yyStringBuf.setLength(0);
        yybegin(STRING);
    }

    /**** espacios ****/
    {ESPACIO}               { }

    /**** error ****/
    . {
        errores.add(new ErrorLexico(
            yytext(), yyline+1, yycolumn+1,
            "Lexico", "Caracter no reconocido"
        ));
    }
}

/**********************ESTADO METADATA*****************************/

<METADATA> {

    "###"                   { yybegin(YYINITIAL); return symbol(sym2.TRIPLE_HASH); }

    "Author:"               { return symbol(sym2.META_AUTHOR); }
    "Fecha:"                { return symbol(sym2.META_FECHA); }
    "Hora:"                 { return symbol(sym2.META_HORA); }
    "Description:"          { return symbol(sym2.META_DESC); }
    "Total de Secciones:"   { return symbol(sym2.META_SECCIONES); }
    "Total de Preguntas:"   { return symbol(sym2.META_PREGUNTAS); }
    "Abiertas:"             { return symbol(sym2.META_ABIERTAS); }
    "Desplegables:"         { return symbol(sym2.META_DESPLEGABLES); }
    "Selección:"            { return symbol(sym2.META_SELECCION); }
    "Múltiples:"            { return symbol(sym2.META_MULTIPLES); }

    /**** valores de metadata — todo lo demas es texto libre ****/
    [^\n#]+                 { return symbol(sym2.META_VALOR, yytext().trim()); }

    {ESPACIO}               { }

    . { }
}

/**********************ESTADO STRING*****************************/

<STRING> {

    \"  {
        String val = yyStringBuf.toString();
        yybegin(YYINITIAL);
        return symbol(sym2.CADENA, val);
    }

    /****emojis version inicial ****/
    "@[:"[^\]]*"]"          { yyStringBuf.append(yytext()); }

    [^\"\n]+                { yyStringBuf.append(yytext()); }

    \n {
        errores.add(new ErrorLexico(
            "Nueva linea en cadena",
            yyline+1, yycolumn+1,
            "Lexico", "Nueva linea no permitida dentro de cadena"
        ));
    }

    <<EOF>> {
        errores.add(new ErrorLexico(
            "EOF en cadena",
            yyline+1, yycolumn+1,
            "Lexico", "Cadena no cerrada"
        ));
        yybegin(YYINITIAL);
        return null;
    }
}

<<EOF>> { return symbol(sym2.EOF); }