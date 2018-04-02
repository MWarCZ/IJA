# Zadání projektu

__Zadání:__ Navrhněte a implementujte aplikaci pro návrh a editaci blokových schémat.
> __Poznámka:__ Zadání definuje podstatné vlastnosti aplikace, které musí být splněny. Předpokládá se, že detaily řešení si doplní řešitelské týmy.

## Specifikace požadavků

1) Základní požadavky
	* aplikace umožní vytvářet, editovat, ukládat a načítat bloková schémata
	* každé schéma má svůj jedinečný název
	* vytvořená schémata lze uložit a opětovně načíst
	* schéma je složeno z bloků a propojů mezi bloky
2) Bloky
	* každý blok má definované vstupní a výstupní porty
	* s každým portem je spojen typ, který je reprezentován množinou dat v podobě dvojic název->hodnota; hodnota bude vždy typu double
	* bloky je možné spojit pouze mezi výstupním a vstupním portem
	* každý blok obsahuje výpočet (vzorce), které transformují hodnoty ze vstupních portů na hodnoty výstupních portů
3) Propojení mezi bloky
	* systém kontroluje kompatibilitu vstupního a výstupního portu propoje (stejný typ dat)
	* typ dat je přiřazen propoji automaticky podle spojených portů
4) Výpočet
	* po sestavení (načtení) schématu je možné provést výpočet
	* systém detekuje cykly v schématu; pokud jsou v schématu cykly, nelze provést výpočet
	* systém požádá o vyplnění dat vstupních portů, která nejsou napojena a poté postupně provádí výpočty jednotlivých bloků podle definovaných vzorců v každém bloku
	* při výpočtu se vždy zvýrazní blok, který je právě přepočítáván
	* výpočet lze krokovat (jeden krok = přepočet jednoho bloku)
5) Další podmínky
	* najetím myši nad propoj se zobrazí aktuální stav dat
	* zvažte způsob jednoduchého rozšiřování systému o nové bloky a data

## Rozšíření pro tříčlenný tým

1) Je možné současně pracovat na více schématech.
2) Složené bloky
	* každé schéma může mít vstupní a výstupní porty a tím tvořit jeden složený blok
	* vytvořené schéma může být součástí jiného schématu jako složený blok; v takovém případě je zobrazen pouze jako blok s příslušnými vstupy a výstupy, lze zobrazit interní schéma tohoto složeného bloku
3) Realizujte způsob jednoduchého rozšiřování systému o nové bloky a data.

## Součást odevzdání

* připravte předem alespoň 5 různých bloků a 3 různé typy (množiny) dat

## Doporučení

* zamyslete se nad použitím vhodných návrhových vzorů
