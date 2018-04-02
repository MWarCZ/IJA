# Pokyny k projektu

## Podmínky vypracování projektu

* Pro realizaci projektu použijte Java SE 8.
* Pro grafické uživatelské rozhraní použijte JavaFX nebo JFC/Swing ([https://docs.oracle.com/javase/8/javase-clienttechnologies.htm](https://docs.oracle.com/javase/8/javase-clienttechnologies.htm)).
* Pro překlad a spuštění projektu bude použita aplikaci [ant](http://ant.apache.org/).
* Projekt bude tvořen následující kořenovou adresářovou strukturou:
```
src/          - (adres.) zdrojové soubory (hierarchie balíků)
examples/     - (adres.) připravené datové soubory
build/        - (adres.) přeložené class soubory
doc/          - (adres.) vygenerovaná programová dokumentace
dest-client/  - (adres.) umístění výsledného jar archivu (+ dalších potřebných) po kompilaci klientské aplikace, 
                         příp. samostatné aplikace (pokud není řešena varianta klient-server), 
                         tento adresář bude představovat adresář spustitelné aplikace
dest-server/  - (adres.) umístění výsledného jar archivu (+ dalších potřebných) po kompilaci serverové aplikace (pokud je 
                         vyžadována při řešení klient-server varianty),
                         tento adresář bude představovat adresář spustitelné aplikace
lib/          - (adres.) externí soubory a knihovny (balíky třetích stran, obrázky apod.), které vaše aplikace využívá
readme.txt    - (soubor) základní popis projektu (název, členové týmu, ...)
rozdeleni.txt - (soubor) soubor obsahuje rozdělení bodů mezi členy týmu (pokud tento soubor neexistuje, předpokládá se 
                         rovnoměrné rozdělení, vizte hodnocení projektu)
build.xml     - (soubor) build file pro aplikaci ant
```
* Všechny zdrojové soubory musí obsahovat na začátku dokumentační komentář se jmény autorů a popisem obsahu.
* Součástí projektu bude programová dokumentace. Dokumentace se vygeneruje při překladu aplikace a bude uložena v adresáři doc.
* Pokud vaše řešení vyžaduje další externí soubory (obrázky, jiné balíky apod.), umístěte je do adresáře lib.
* Přeložení aplikace:
  * v kořenovém adresáři se provede příkazem ant compile
  * zkompilují se zdrojové texty, class soubory budou umístěny v adresáři build
  * vytvoří se programová dokumentace a uloží se do adresáře doc
  * vytvoří se jar archivy s názvy ija-client.jar v adresáři dest-client a ija-server.jar v adresáři dest-server (pokud je vyžadováno); do těchto adresářů se vytvoří/nakopírují další potřebné soubory a archivy
* Spuštění aplikace:
  * spouštět se budou vytvořené archivy jar
  * v kořenovém adresáři se provede příkazem ant run
  * spustí se klientská aplikace, případně server a dvě klientské aplikace (podle realizované varianty)
  
## Podmínky odevzdání projektu

* Adresářovou strukturu umístěte do kořenového adresáře s názvem, který odpovídá loginu vedoucího týmu, např. xloginXX. Pro obsah podadresářů platí:
  * Adresáře build, doc, a dest-* budou prázdné.
  * V adresáři examples budou ukázkové datové soubory (pokud je zadání vyžaduje).
  * Adresář lib
    * __obsahuje pouze skript s názvem__ `get-libs.sh`, který po spuštění stáhne z internetu požadované externí knihovny či soubory (předpokládejte prostředí Unix/Linux a přítomnost nástroje `wget` (v. 1.15) a zip (v.3.0))
    * externí knihovny lze stáhnout přímo ze zdroje, další soubory můžete dočasně umístit na váš webový prostor
    * pokud vaše řešení žádné další knihovny a soubory nepožaduje, nedělá skript nic
* Obsah kořenového adresáře zabalte do archivu zip:
  * Název archivu bude stejný jako název kořenového adresáře (s příponou .zip), tj. login vedoucího týmu (např. xloginXX.zip). Po rozbalení archivu vznikne adresářová struktura definovaná výše včetně adresáře xloginXX.
  * Archiv zip odevzdá pouze vedoucí týmu do informačního systému, termín Projekt.

## Hodnocení projektu

* Projekt je hodnocen na stupnici 0-100. Po ohodnocení dojde k přepočtu na body pro jednotlivé členy týmu, a to rovnoměrně (každý získá stejný počet bodů, který odpovídá hodnocení projektu). Hodnocení každého člena týmu lze přerozdělit:
  * přerozdělení bodů je možné v rozsahu -50% až +50%
  * při přerozdělení může být vzata v úvahu aktivita jednotlivých členů
  * pokud máte dojem, že některý člen týmu nepracuje a nezaslouží si ani 50% z hodnocení, je nutno toto řešit individuálně s dr. Kočím před odevzdáním projektu
  * přerozdělení bodů musí být zapsáno v souboru rozdeleni.txt v následujícím formátu (uvádí se procenta, která získává člen týmu; součet musí odpovídat hodnotě pocet_clenu * 100): 
 ```
 xloginXX: 50
 xloginYY: !50
 ```
* Hodnocení zahrnuje kvalitu OO návrhu, kvalitu implementace, dodržování stylu psaní programu (odsazování, kvalita komentářů, vhodné identifikátory) a především funkčnost programu.
* Při návrhu postupujte tak, aby výsledný program byl dobře použitelný. Hodnotit se bude nejen to, zda splňuje kladené požadavky, ale také JAK je splňuje.
* Pro získání zápočtu musí mít každý člen týmu (po příp. korekcích v rámci týmu) alespoň 50% z maxima hodnocení projektu.
* __Doporučuji po vytvoření archivu přenést a vyzkoušet přeložitelnost a spuštění aplikace v jiném prostředí (např. merlin.fit.vutbr.cz).__
* __Pokud nepůjde projekt přeložit, bude hodnocen 0 body!__

