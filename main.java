public class MyClass {
    public static void main(String args[]) {
    
        System.out.println("ej3");
        //mal hecho
        //sacar codigo zona
        String squeryCodZona = "for $zona in /zonas/zona " + 
        "return $zona/codizona";
        XQPreparedExpresion consultaCodZona = connexio.prepareExpression(squeryCodZona);
        XQResultSequence resultadoCodZona = consultaCodZona.executeQuery();
            
        //sacar nombre zona
        String squeryNomZona = "for $zona in /zonas/zona " + 
        "return $zona/nombre";
        XQPreparedExpresion consultaNomZona = connexio.prepareExpression(squeryNomZona);
        XQResultSequence resultadoNomZona = consultaNomZona.executeQuery();
        
        
        while (resultadoCodZona.Next() && resultadoNomZona.Next()){
            String codZona = resultadoCodZona.getItemAsString(null);
            String NomZona = resultadoNomZona.getItemAsString(null);
            
            //sacar valores productos
            String l1 = "<prodzona"+ codZona + "> \n";
            String l2 = "\t <nom_zona>" + NomZona + "</nom_zona>\n";
            String l3 = "";
            
            //sacar valores productos
            String squeryProd = "for $codiProd in /productos/produc[cod_zona= " + codZona +"]/cod_prod" + 
            "\n for $denom in /productos/produc[cod_zona= " + codZona +"]/denominacion" + 
            "\n return <producto>{$codiProd,$denom}</producto>";
            XQPreparedExpresion consultaProd = connexio.prepareExpression(squeryProduct);
            XQResultSequence resultadoProd = consultaProd.executeQuery();
            
            while (resultadoProd.next()){
                l3 += "\t"+ resultadoProd.getItemAsString(null) +"\n";
            }
            
            String l7 = "</prodzona"+ codZona + "> \n";

            String resultadoFinal = l1 + l2 + l3 + l7;
            
            String nombreFichero = "ProdZona" + codZona + ".xml";
            
              //guardar ficheros distintos
              guardarFichero(resultadoFinal,nombreFichero);
              
        }
    }
}
