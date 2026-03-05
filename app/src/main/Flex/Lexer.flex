//imports

%% //serpador de area
//atributos

%public
%unicode
%class Lexer
%standalone
%line
%column




%init{
//constructor
%init}


/**********************macros****************************************/

ESPACIO = [ \t\r\n]+
ID = [a-zA-Z_][a-zA-Z0-9_]*
NUMERO = [0-9]+(\.[0-9]+)?
CADENA = \"([^\"\\]|\\.)*\"

HEX_COLOR = #[0-9a-fA-F]{6}
RGB_COLOR = \([0-9]{1,3},[0-9]{1,3},[0-9]{1,3}\)
HSL_COLOR = \<[0-9]{1,3},[0-9]{1,3},[0-9]{1,3}\>
//EMOJI = @\\[:[a-zA-Z0-9()^<3]+:\\]

COMODIN = \?

/****************************codigo de usuario*********************/
%{
private void printToken(String tipo) {
      System.out.println(
              "Token: " + tipo +
              " | Lexema: " + yytext() +
              " | Linea: " + (yyline + 1) +
              " | Columna: " + (yycolumn + 1)
      );
}

%}

%%
//declaracion de elementos del lenguaje y acciones

"SECTION"                           {   printToken("SECTION"); }
"TABLE"                             {   printToken("TABLE"); }
"TEXT"                              {   printToken("TEXT"); }
"OPEN_QUESTION"                     {   printToken("OPEN_QUESTION"); }
"DROP_QUESTION"                     {   printToken("DROP_QUESTION"); }
"SELECT_QUESTION"                   {   printToken("SELECT_QUESTION"); }
"MULTIPLE_QUESTION"                 {   printToken("MULTIPLE_QUESTION"); }

"number"                            {   printToken("number"); }
"string"                            {   printToken("string"); }
"special"                           {   printToken("special"); }

"VERTICAL"                          {   printToken("VERTICAL"); }
"HORIZONTAL"                        {   printToken("HORIZONTAL"); }

"styles"                            {   printToken("styles"); }
"color"                             {   printToken("color"); }
"border"                            {   printToken("border"); }

"MONO"                              {   printToken("MONO"); }
"SANS_SERIF"                        {   printToken("SANS_SERIF"); }
"CURSIVE"                           {   printToken("CURSIVE"); }

"IF"                                {   printToken("IF"); }
"ELSE"                              {   printToken("ELSE"); }
"WHILE"                             {   printToken("WHILE"); }
"DO"                                {   printToken("DO"); }
"FOR"                               {   printToken("FOR"); }
"in"                                {   printToken("IN"); }

"draw"                              { printToken("DRAW");}

"who_is_that_pokemon"               {   printToken("who_is_that_pokemon"); }

"width"        {   printToken("width"); }
"pointX"        {   printToken("pointX"); }
"height"        {   printToken("height"); }
"pointY"        {   printToken("pointY"); }
"orientation"   {   printToken("orientation"); }
"elements"      {   printToken("elements"); }
"options"       {   printToken("options"); }
"correct"       {   printToken("correct"); }
"content"       {   printToken("content"); }
"label"         {   printToken("label"); }


"background color"  { printToken("background_color"); }
"font family"       { printToken("font_family");}
"text size"         { printToken("text_size"); }

"LINE"          { printToken("LINE"); }
"DOTTED"        { printToken("DOTTED"); }
"DOUBLE"        { printToken("DOUBLE"); }

"RED"           { printToken("RED");}
"BLUE"          { printToken("BLUE");}
"GREEN"         { printToken("GREEN");}
"PURPLE"        { printToken("PURPLE");}
"SKY"           { printToken("SKY");}
"YELLOW"        { printToken("YELLOW");}
"BLACK"         { printToken("BLACK");}
"WHITE"         { printToken("WHITE");}


/*********************simbolos*****************************/

"["             { printToken("LCOR");}
"]"             { printToken("RCOR");}
"{"             { printToken("LKEY");}
"}"             { printToken("RKEY");}
","             { printToken("COMA");}
"."             { printToken("PUNTO");}
":"             { printToken("DOS_PUNTOS");}
";"             { printToken("PUNTO_COMA");}
"="             { printToken("ASIGNACION");}
".."            { printToken("RANGO");}

/************************operadores aritmeticos****************************/
"+"     { printToken("SUMA"); }
"-"     { printToken("RESTA"); }
"*"     { printToken("MULTIPLICACION"); }
"/"     { printToken("DIVISION"); }
"^"     { printToken("POTENCIA"); }
"%"     { printToken("MODULO"); }
"("     { printToken("PARENTESIS_ABRE"); }
")"     { printToken("PARENTESIS_CIERRA"); }



/********************** operadores de comparacion *****************************/
"=="    { printToken("IGUALDAD"); }
">="     { printToken("MAYOR_IGUAL"); }
">"    { printToken("MAYOR"); }
"<="     { printToken("MENOR_IGUAL"); }
"<"    { printToken("MENOR"); }
"!!"    { printToken("DIFERENTE"); }

/********************************operadores logicos*********************************/
"||"    { printToken("OR"); }
"&&"    { printToken("AND"); }
"~"     { printToken("NEGACION"); }

//comodin
{COMODIN}           { printToken("COMODIN");}

/***********************colores*****************************************************/
{HEX_COLOR} { printToken("HEX_COLOR"); }
{RGB_COLOR} { printToken("RGB_COLOR"); }
{HSL_COLOR} { printToken("HSL_COLOR"); }




/***********************emojis***************************************************/
//{EMOJI}         { printToken("EMOJI");}
/********************************litelares*****************************/

{NUMERO}                            { printToken("NUMERO");}
{CADENA}                            { printToken("CADENA"); }

/********************************identificadores********************/

{ID}                                { printToken("IDENTIFICADOR"); }

/*******************************cometarios***********************************/
\$[^\n]*                                 {   /*comentario de una linea*/}
"/*"~"*/"                                { /* comentario multilínea */ }

{ESPACIO}                             {/*ignorar termporalmente*/}

.                                   { System.out.println("ERROR Lexico: " + yytext() +
                                                            " | Linea: " + (yyline+1) +
                                                            " | Columna: " + (yycolumn+ 1));}


<<EOF>>                        { System.out.println("final del archivo");
                                    return YYEOF;}