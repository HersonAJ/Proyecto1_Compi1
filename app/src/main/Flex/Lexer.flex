package com.example.proyecto1_compi1.Analizadores;
//imports
import com.example.proyecto1_compi1.models.ErrorLexico;
import java.util.ArrayList;
import java.util.List;
import java_cup.runtime.Symbol;

%% //serpador de area
//atributos

%public
%unicode
%class Lexer
%cup
%line
%column
%type java_cup.runtime.Symbol
%state STRING

%init{
//constructor
%init}


/**********************macros****************************************/

ESPACIO = [ \t\r\n]+
ID = [a-zA-Z_][a-zA-Z0-9_]*
NUMERO = [0-9]+(\.[0-9]+)?

HEX_COLOR = #[0-9a-fA-F]{6}
HSL_COLOR = \<[ \t]*[0-9]{1,3}[ \t]*,[ \t]*[0-9]{1,3}[ \t]*,[ \t]*[0-9]{1,3}[ \t]*\>

COMODIN = \?

/***********************************macros para emojis********************************/

EMOJI_SMILE= @\[:\)+\]
EMOJI_SMILE_TXT = @\[:smile:\]

EMOJI_SAD = @\[:\(+\]
EMOJI_SAD_TXT = @\[:sad:\]

EMOJI_SERIOUS = @\[:\|+\]
EMOJI_SERIOUS_TXT = @\[:serious:\]

EMOJI_HEART = @\[<+3+\]
EMOJI_HEART_TXT = @\[:heart:\]

EMOJI_STAR = @\[:star:\]
EMOJI_STAR_NUM = @\[:star:[0-9]+\]
EMOJI_STAR_NUM_ALT = @\[:star-[0-9]+\]

EMOJI_CAT = @\[:\^\^:\]
EMOJI_CAT_TXT = @\[:cat:\]

/****************************codigo de usuario*********************/
%{

private List<ErrorLexico> errores = new ArrayList<>();

public List<ErrorLexico> getErrores() {
    return errores;
}

private StringBuilder yyStringBuf = new StringBuilder();
private int stringStartLine = 0;
private int stringStartCol = 0;
private final int MAX_STRING_LENGTH = 2000;

private void appendToStringBuf(String s) {
    if (yyStringBuf.length() + s.length() > MAX_STRING_LENGTH) {
        errores.add(new ErrorLexico("Literal demasiado largo", yyline+1, yycolumn+1, "Lexico", "Cadena excede tamaño máximo"));
    } else {
        yyStringBuf.append(s);
    }
}

private Symbol symbol(int type) {
    return new Symbol(type, yyline+1, yycolumn+1);
}

private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline+1, yycolumn+1, value);
}

%}

%%

/***********************ESTADO INICIAL*****************************/

<YYINITIAL>{

"SECTION"                           {   return symbol(sym.SECTION); }
"TABLE"                             {   return symbol(sym.TABLE); }
"TEXT"                              {   return symbol(sym.TEXT); }
"OPEN_QUESTION"                     {   return symbol(sym.OPEN_QUESTION); }
"DROP_QUESTION"                     {   return symbol(sym.DROP_QUESTION); }
"SELECT_QUESTION"                   {   return symbol(sym.SELECT_QUESTION); }
"MULTIPLE_QUESTION"                 {   return symbol(sym.MULTIPLE_QUESTION); }

"number"                            {   return symbol(sym.NUMBER_TYPE); }
"NUMBER"                            {   return symbol(sym.NUMBER_PK); }
"string"                            {   return symbol(sym.STRING_TYPE); }
"special"                           {   return symbol(sym.SPECIAL); }

"VERTICAL"                          {   return symbol(sym.VERTICAL); }
"HORIZONTAL"                        {   return symbol(sym.HORIZONTAL); }

"styles"                            {   return symbol(sym.STYLES); }

"MONO"                              {   return symbol(sym.MONO); }
"SANS_SERIF"                        {   return symbol(sym.SANS_SERIF); }
"CURSIVE"                           {   return symbol(sym.CURSIVE); }

"IF"                                {   return symbol(sym.IF); }
"ELSE"                              {   return symbol(sym.ELSE); }
"WHILE"                             {   return symbol(sym.WHILE); }
"DO"                                {   return symbol(sym.DO); }
"FOR"                               {   return symbol(sym.FOR); }
"in"                                {   return symbol(sym.IN); }

"draw"                              {   return symbol(sym.DRAW); }

"who_is_that_pokemon"               {   return symbol(sym.POKEMON); }

"width"                             {   return symbol(sym.WIDTH); }
"pointX"                            {   return symbol(sym.POINTX); }
"height"                            {   return symbol(sym.HEIGHT); }
"pointY"                            {   return symbol(sym.POINTY); }
"orientation"                       {   return symbol(sym.ORIENTATION); }
"elements"                          {   return symbol(sym.ELEMENTS); }
"options"                           {   return symbol(sym.OPTIONS); }
"correct"                           {   return symbol(sym.CORRECT); }
"content"                           {   return symbol(sym.CONTENT); }
"label"                             {   return symbol(sym.LABEL); }


"\"background color\""              { return symbol(sym.BACKGROUND_COLOR); }
"\"font family\""                   { return symbol(sym.FONT_FAMILY); }
"\"text size\""                     { return symbol(sym.TEXT_SIZE); }
"\"color\""                         { return symbol(sym.COLOR); }
"\"border\""                        { return symbol(sym.BORDER); }

"LINE"                              {   return symbol(sym.LINE); }
"DOTTED"                            {   return symbol(sym.DOTTED); }
"DOUBLE"                            {   return symbol(sym.DOUBLE); }

"RED"                               {   return symbol(sym.RED); }
"BLUE"                              {   return symbol(sym.BLUE); }
"GREEN"                             {   return symbol(sym.GREEN); }
"PURPLE"                            {   return symbol(sym.PURPLE); }
"SKY"                               {   return symbol(sym.SKY); }
"YELLOW"                            {   return symbol(sym.YELLOW); }
"BLACK"                             {   return symbol(sym.BLACK); }
"WHITE"                             {   return symbol(sym.WHITE); }


/*********************simbolos*****************************/

"["                                 {   return symbol(sym.LCORCHETE); }
"]"                                 {   return symbol(sym.RCORCHETE); }
"{"                                 {   return symbol(sym.LLLAVE); }
"}"                                 {   return symbol(sym.RLLAVE); }
","                                 {   return symbol(sym.COMA); }
".."                                {   return symbol(sym.RANGO); }
"."                                 {   return symbol(sym.PUNTO); }
":"                                 {   return symbol(sym.DOS_PUNTOS); }
";"                                 {   return symbol(sym.PUNTO_COMA); }
"="                                 {   return symbol(sym.ASIGNACION); }

/************************operadores aritmeticos****************************/
"+"                                 {   return symbol(sym.SUMA); }
"-"                                 {   return symbol(sym.RESTA); }
"*"                                 {   return symbol(sym.MULTI); }
"/"                                 {   return symbol(sym.DIV); }
"^"                                 {   return symbol(sym.POTENCIA); }
"%"                                 {   return symbol(sym.MOD); }
"("                                 {   return symbol(sym.LPAREN); }
")"                                 {   return symbol(sym.RPAREN); }



/********************** operadores de comparacion *****************************/
">="                                {   return symbol(sym.MAYOR_IGUAL); }
"<="                                {   return symbol(sym.MENOR_IGUAL); }
">"                                 {   return symbol(sym.MAYOR); }
"<"                                 {   return symbol(sym.MENOR); }
"!!"                                {   return symbol(sym.DIFERENTE); }
"=="                                {   return symbol(sym.IGUAL); }

/********************************operadores logicos*********************************/
"||"                                {   return symbol(sym.OR); }
"&&"                                {   return symbol(sym.AND); }
"~"                                 {   return symbol(sym.NOT); }

//comodin
{COMODIN}                           {   return symbol(sym.COMODIN); }

/***********************colores*****************************************************/
{HEX_COLOR}                         {   return symbol(sym.HEX_COLOR, yytext()); }
{HSL_COLOR}                         {   return symbol(sym.HSL_COLOR, yytext()); }

/***********************numeros********/

{NUMERO}                            {   return symbol(sym.NUMERO); }

/***********************inicio cadena*****************************************************/

\" {
    stringStartLine = yyline + 1;
    stringStartCol = yycolumn + 1;
    yyStringBuf.setLength(0);
    yybegin(STRING);
}

/***********************identificadores*****************************************************/

{ID}                                {   return symbol(sym.ID, yytext()); }

/*******************************cometarios***********************************/
\$[^\n]*                            {   /*comentario de una linea*/}

"/*"~"*/"                           { /* comentario multilínea */ }

/***********************espacios*****************************************************/

{ESPACIO}                           { }

/***********************error*****************************************************/

.                     { errores.add( new ErrorLexico( yytext(), yyline+1, yycolumn+1, "Lexico", "Caracter no reconocido")); }

}

/*************************** ESTADO STRING **************************/

<STRING>{

    \" {
        String finalLexema = yyStringBuf.toString();
        yybegin(YYINITIAL);
        return new Symbol(sym.CADENA, stringStartLine, stringStartCol, finalLexema);
    }

/*********************emojis*************************/

    {EMOJI_SMILE} | {EMOJI_SMILE_TXT}         { appendToStringBuf("@[:smile:]"); }

    {EMOJI_SAD} | {EMOJI_SAD_TXT}             { appendToStringBuf("@[:sad:]"); }

    {EMOJI_SERIOUS} | {EMOJI_SERIOUS_TXT}     { appendToStringBuf("@[:serious:]"); }

    {EMOJI_HEART} | {EMOJI_HEART_TXT}         { appendToStringBuf("@[:heart:]"); }

    {EMOJI_CAT} | {EMOJI_CAT_TXT}             { appendToStringBuf("@[:cat:]"); }

    {EMOJI_STAR}                              { appendToStringBuf("@[:star:]"); }

    {EMOJI_STAR_NUM} {

        String lex = yytext();

        int num = Integer.parseInt(
            lex.substring(7, lex.length()-2)
        );

        for(int i=0;i<num;i++){
            appendToStringBuf("@[:star:]");
        }
    }

    {EMOJI_STAR_NUM_ALT} {

        String lex = yytext(); // @[:star-3]

        int num = Integer.parseInt(
            lex.substring(7, lex.length()-1)
        );

        for(int i=0;i<num;i++){
            appendToStringBuf("@[:star:]");
        }
    }

/*********************texto normal *********************/

    [^\"@\n]+                             { appendToStringBuf(yytext()); }

    "@"                                   { appendToStringBuf("@"); }

/*********************errores*************************/

    \n { errores.add(new ErrorLexico( "Nueva linea en literal de cadena", yyline+1, yycolumn+1, "Lexico", "Nueva linea no permitida dentro de cadena" )); }

    .   { appendToStringBuf(yytext()); }

    <<EOF>> {errores.add(new ErrorLexico("EOF dentro de literal de cadena",yyline+1, yycolumn+1,"Lexico","Cadena no cerrada"));
        yybegin(YYINITIAL);
        return null;
    }
}

/***********************EOF*****************************************************/

<<EOF>>               { return symbol(sym.EOF);}