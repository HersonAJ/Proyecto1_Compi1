package com.example.proyecto1_compi1.Analizadores;
//imports
import com.example.proyecto1_compi1.models.Token;
import com.example.proyecto1_compi1.models.ErrorLexico;
import java.util.ArrayList;
import java.util.List;

%% //serpador de area
//atributos

%public
%unicode
%class Lexer
%line
%column
%type com.example.proyecto1_compi1.models.Token
%state STRING

%init{
//constructor
%init}


/**********************macros****************************************/

ESPACIO = [ \t\r\n]+
ID = [a-zA-Z_][a-zA-Z0-9_]*
NUMERO = [0-9]+(\.[0-9]+)?

HEX_COLOR = #[0-9a-fA-F]{6}
RGB_COLOR = \([0-9]{1,3},[0-9]{1,3},[0-9]{1,3}\)
HSL_COLOR = \<[0-9]{1,3},[0-9]{1,3},[0-9]{1,3}\>

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

%}

%%

/***********************ESTADO INICIAL*****************************/

<YYINITIAL>{

"SECTION"                           {   return new Token("PALABRA RESERVADA", yytext(), yyline+1, yycolumn+1); }
"TABLE"                             {   return new Token("PALABRA RESERVADA", yytext(), yyline+1, yycolumn+1); }
"TEXT"                              {   return new Token("PALABRA RESERVADA", yytext(), yyline+1, yycolumn+1); }
"OPEN_QUESTION"                     {   return new Token("PALABRA RESERVADA", yytext(), yyline+1, yycolumn+1); }
"DROP_QUESTION"                     {   return new Token("PALABRA RESERVADA", yytext(), yyline+1, yycolumn+1); }
"SELECT_QUESTION"                   {   return new Token("PALABRA RESERVADA", yytext(), yyline+1, yycolumn+1); }
"MULTIPLE_QUESTION"                 {   return new Token("PALABRA RESERVADA", yytext(), yyline+1, yycolumn+1); }

"number"                            {   return new Token("PALABRA RESERVADA", yytext(), yyline+1, yycolumn+1); }
"string"                            {   return new Token("PALABRA RESERVADA", yytext(), yyline+1, yycolumn+1); }
"special"                           {   return new Token("TIPO_SPECIAL", yytext(), yyline+1, yycolumn+1); }

"VERTICAL"                          {   return new Token("PALABRA RESERVADA", yytext(), yyline+1, yycolumn+1); }
"HORIZONTAL"                        {   return new Token("PALABRA RESERVADA", yytext(), yyline+1, yycolumn+1); }

"styles"                            {   return new Token("PALABRA RESERVADA", yytext(), yyline+1, yycolumn+1); }
"color"                             {   return new Token("PALABRA RESERVADA", yytext(), yyline+1, yycolumn+1); }
"border"                            {   return new Token("PALABRA RESERVADA", yytext(), yyline+1, yycolumn+1); }

"MONO"                              {   return new Token("PALABRA RESERVADA", yytext(), yyline+1, yycolumn+1); }
"SANS_SERIF"                        {   return new Token("PALABRA RESERVADA", yytext(), yyline+1, yycolumn+1); }
"CURSIVE"                           {   return new Token("PALABRA RESERVADA", yytext(), yyline+1, yycolumn+1); }

"IF"                                {   return new Token("PALABRA RESERVADA", yytext(), yyline+1, yycolumn+1); }
"ELSE"                              {   return new Token("PALABRA RESERVADA", yytext(), yyline+1, yycolumn+1); }
"WHILE"                             {   return new Token("PALABRA RESERVADA", yytext(), yyline+1, yycolumn+1); }
"DO"                                {   return new Token("PALABRA RESERVADA", yytext(), yyline+1, yycolumn+1); }
"FOR"                               {   return new Token("PALABRA RESERVADA", yytext(), yyline+1, yycolumn+1); }
"in"                                {   return new Token("PALABRA RESERVADA", yytext(), yyline+1, yycolumn+1); }

"draw"                              {   return new Token("PALABRA RESERVADA", yytext(), yyline+1, yycolumn+1); }

"who_is_that_pokemon"               {   return new Token("PALABRA RESERVADA", yytext(), yyline+1, yycolumn+1); }

"width"         {   return new Token("CONFIG", yytext(), yyline+1, yycolumn+1); }
"pointX"        {   return new Token("CONFIG", yytext(), yyline+1, yycolumn+1); }
"height"        {   return new Token("CONFIG", yytext(), yyline+1, yycolumn+1); }
"pointY"        {   return new Token("CONFIG", yytext(), yyline+1, yycolumn+1); }
"orientation"   {   return new Token("CONFIG", yytext(), yyline+1, yycolumn+1); }
"elements"      {   return new Token("CONFIG", yytext(), yyline+1, yycolumn+1); }
"options"       {   return new Token("CONFIG", yytext(), yyline+1, yycolumn+1); }
"correct"       {   return new Token("CONFIG", yytext(), yyline+1, yycolumn+1); }
"content"       {   return new Token("CONFIG", yytext(), yyline+1, yycolumn+1); }
"label"         {   return new Token("CONFIG", yytext(), yyline+1, yycolumn+1); }


"background color"  {   return new Token("PROPIEDAD", yytext(), yyline+1, yycolumn+1); }
"font family"       {   return new Token("PROPIEDAD", yytext(), yyline+1, yycolumn+1); }
"text size"         {   return new Token("PROPIEDAD", yytext(), yyline+1, yycolumn+1); }

"LINE"          {   return new Token("PROPIEDAD", yytext(), yyline+1, yycolumn+1); }
"DOTTED"        {   return new Token("PROPIEDAD", yytext(), yyline+1, yycolumn+1); }
"DOUBLE"        {   return new Token("PROPIEDAD", yytext(), yyline+1, yycolumn+1); }

"RED"           {   return new Token("COLOR_BASE", yytext(), yyline+1, yycolumn+1); }
"BLUE"          {   return new Token("COLOR_BASE", yytext(), yyline+1, yycolumn+1); }
"GREEN"         {   return new Token("COLOR_BASE", yytext(), yyline+1, yycolumn+1); }
"PURPLE"        {   return new Token("COLOR_BASE", yytext(), yyline+1, yycolumn+1); }
"SKY"           {   return new Token("COLOR_BASE", yytext(), yyline+1, yycolumn+1); }
"YELLOW"        {   return new Token("COLOR_BASE", yytext(), yyline+1, yycolumn+1); }
"BLACK"         {   return new Token("COLOR_BASE", yytext(), yyline+1, yycolumn+1); }
"WHITE"         {   return new Token("COLOR_BASE", yytext(), yyline+1, yycolumn+1); }


/*********************simbolos*****************************/

"["             {   return new Token("SIMBOLO", yytext(), yyline+1, yycolumn+1); }
"]"             {   return new Token("SIMBOLO", yytext(), yyline+1, yycolumn+1); }
"{"             {   return new Token("SIMBOLO", yytext(), yyline+1, yycolumn+1); }
"}"             {   return new Token("SIMBOLO", yytext(), yyline+1, yycolumn+1); }
","             {   return new Token("SIMBOLO", yytext(), yyline+1, yycolumn+1); }
".."            {   return new Token("SIMBOLO", yytext(), yyline+1, yycolumn+1); }
"."             {   return new Token("SIMBOLO", yytext(), yyline+1, yycolumn+1); }
":"             {   return new Token("SIMBOLO", yytext(), yyline+1, yycolumn+1); }
";"             {   return new Token("SIMBOLO", yytext(), yyline+1, yycolumn+1); }
"="             {   return new Token("SIMBOLO", yytext(), yyline+1, yycolumn+1); }

/************************operadores aritmeticos****************************/
"+"     {   return new Token("OP_ARITMETICO", yytext(), yyline+1, yycolumn+1); }
"-"     {   return new Token("OP_ARITMETICO", yytext(), yyline+1, yycolumn+1); }
"*"     {   return new Token("OP_ARITMETICO", yytext(), yyline+1, yycolumn+1); }
"/"     {   return new Token("OP_ARITMETICO", yytext(), yyline+1, yycolumn+1); }
"^"     {   return new Token("OP_ARITMETICO", yytext(), yyline+1, yycolumn+1); }
"%"     {   return new Token("OP_ARITMETICO", yytext(), yyline+1, yycolumn+1); }
"("     {   return new Token("LPAREN", yytext(), yyline+1, yycolumn+1); }
")"     {   return new Token("RPAREN", yytext(), yyline+1, yycolumn+1); }



/********************** operadores de comparacion *****************************/
">="     {   return new Token("OP_COMPARACION", yytext(), yyline+1, yycolumn+1); }
"<="     {   return new Token("OP_COMPARACION", yytext(), yyline+1, yycolumn+1); }
">"      {   return new Token("OP_COMPARACION", yytext(), yyline+1, yycolumn+1); }
"<"      {   return new Token("OP_COMPARACION", yytext(), yyline+1, yycolumn+1); }
"!!"     {   return new Token("OP_COMPARACION", yytext(), yyline+1, yycolumn+1); }
"=="     {   return new Token("OP_COMPARACION", yytext(), yyline+1, yycolumn+1); }

/********************************operadores logicos*********************************/
"||"    {   return new Token("OP_LOGICO", yytext(), yyline+1, yycolumn+1); }
"&&"    {   return new Token("OP_LOGICO", yytext(), yyline+1, yycolumn+1); }
"~"     {   return new Token("OP_LOGICO", yytext(), yyline+1, yycolumn+1); }

//comodin
{COMODIN}           {   return new Token("COMODIN", yytext(), yyline+1, yycolumn+1); }

/***********************colores*****************************************************/
{HEX_COLOR} { return new Token("HEX_COLOR", yytext(), yyline+1, yycolumn+1); }
{RGB_COLOR} { return new Token("RGB_COLOR", yytext(), yyline+1, yycolumn+1); }
{HSL_COLOR} { return new Token("HSL_COLOR", yytext(), yyline+1, yycolumn+1); }

/***********************numeros********/

{NUMERO}                            {   return new Token("NUMERO", yytext(), yyline+1, yycolumn+1); }

/***********************inicio cadena*****************************************************/

\" {
    stringStartLine = yyline + 1;
    stringStartCol = yycolumn + 1;
    yyStringBuf.setLength(0);
    yybegin(STRING);
}

/***********************identificadores*****************************************************/

{ID}                                {   return new Token("ID", yytext(), yyline+1, yycolumn+1); }

/*******************************cometarios***********************************/
\$[^\n]*                                 {   /*comentario de una linea*/}

"/*"~"*/"                                { /* comentario multilínea */ }

/***********************espacios*****************************************************/

{ESPACIO} { }

/***********************error*****************************************************/

.                     { errores.add( new ErrorLexico( yytext(), yyline+1, yycolumn+1, "Lexico", "Caracter no reconocido")); }

}

/*************************** ESTADO STRING **************************/

<STRING>{

    \" {
        String finalLexeme = yyStringBuf.toString();
        yybegin(YYINITIAL);
        return new Token("CADENA", finalLexeme, stringStartLine, stringStartCol);
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

<<EOF>>               { return null;}