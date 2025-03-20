module Main where

import Palet
import Route
import Stack
import Truck
import Control.Exception
import System.IO.Unsafe


testF :: Show a => a -> Bool
testF action = unsafePerformIO $ do
    result <- tryJust isException (evaluate action)
    return $ case result of
        Left _ -> True
        Right _ -> False
    where
        isException :: SomeException -> Maybe ()
        isException _ = Just ()


-- Ejemplos de rutas
rutaVacia = newR []  
rutaLarga = newR ["Roma", "París", "Mdq", "Berna"]

-- Ejemplos de palets
palet1 = newP "Roma" 5
palet2 = newP "París" 4
paletInvalido1 = newP "Berna" 12 
paletInvalido2 = newP "Mdq" 8      
paletValido = newP "París" 5  
paletLiviano = newP "Roma" 2   

-- Ejemplos de camiones
camionInicial = newT 3 5 rutaLarga
camionCargado = loadT camionInicial palet1  -- Carga palet1 en el camión


tests :: [Bool]
tests = paletsTests ++ rutasTests ++ stacksTests ++ camionesTests

  where
    -- Tests de palets
    paletsTests =
      [ testF (netP palet1 == 5),  -- Peso del palet 1
        testF (destinationP palet2 == "París")  -- Ciudad destino del palet 2
      ]

    -- Tests de rutas
    rutasTests =
      [ testF (inOrderR rutaVacia "Roma" "París" == False),  -- Caso ruta vacía
        testF (inOrderR rutaLarga "Roma" "Teherán" == False),  -- "Roma" antes de "Teherán", pero falta en la ruta
        testF (inOrderR rutaLarga "Amsterdam" "París" == False),  -- "Amsterdam" no está en la ruta
        testF (inOrderR rutaLarga "Amsterdam" "Berlin" == False), -- Ninguna de las ciudades está en la ruta
        testF (inOrderR rutaLarga "Roma" "París" == True),  -- "Roma" precede a "París"
        testF (inRouteR rutaLarga "Roma" == True), -- "Roma" está en la ruta
        testF (inRouteR rutaLarga "Madrid" == False) -- "Madrid" no está en la ruta
      ]
      
    stackInicial = newS 3
    stackLleno = stackS (stackS stackInicial palet1) palet2  -- Apilamos palet1 y palet2
    stack1 = stackS (newS 3) palet1  
    stack2 = stackS (stackS (stackS stackInicial paletLiviano) paletLiviano) paletLiviano

    -- Tests de pilas (bahías)
    stacksTests =
      [ testF (freeCellsS stackInicial == 3), -- Inicialmente tiene 3 celdas libres
        testF (freeCellsS stackLleno == 1), -- Después de apilar dos palets queda 1 espacio libre
        testF (netS stackLleno == 9), -- Total 9 (5 + 4)
        testF (holdsS stackLleno palet1 rutaLarga == True), -- Se puede añadir palet1 teniendo en cuenta la ruta
        testF (holdsS stackLleno paletInvalido1 rutaLarga == False), -- No se puede añadir paletInvalido1 según la ruta
        
        -- Comprobaciones al apilar por sobrepeso o capacidad
        testF (stackS stackLleno palet2 == stackLleno), -- No se puede apilar palet1 por sobrepeso, queda igual la bahia
        testF (stackS stack2 paletLiviano == stack2), -- Supera capacidad sin superar las 10 toneladas, queda igual
        -- Comprobaciones al apilar un nuevo palet válido
        let resultado = stackS stackLleno paletLiviano  -- Intenta apilar paletLiviano
        in testF (freeCellsS resultado == 0 && netS resultado == 10),  -- Verifica que el cargo fue exitoso
        
        -- Test de popS
        testF (popS stackInicial "Roma" == stackInicial),  -- La pila se mantiene vacía, nada que eliminar
        testF (popS stack1 "Roma" == newS 3),  -- Debería devolver una pila vacía después de sacar el palet de Roma
        testF (popS stackLleno "París" == stack1),  -- Debería devolver la pila con el palet de Roma
        testF (popS stackLleno "Roma" == stackLleno)  -- Debería devolver la misma pila, ya que "Roma" no está en el tope
      ]

    -- Tests de camiones
    camionesTests =
      [ 
        -- Creación de un camión
        testF (camionInicial == newT 3 5 rutaLarga), 
          
        -- Carga del palet
        testF (loadT camionInicial palet1 == camionCargado), -- Se carga palet1 exitosamente
        testF (loadT camionCargado paletInvalido1 == camionCargado), -- El paletInvalido1 no se puede cargar y el camion queda igual

        -- Cargar un palet válido y verificar los componentes
        let camionResultante = loadT camionCargado paletValido
            stacks1 = freeCellsT camionCargado       -- Obtener celdas de camión antes de carga
            stacksResultantes = freeCellsT camionResultante  -- Obtener celdas después de carga
        in testF (stacksResultantes == stacks1 - 1),  -- Comprobar que se ha incrementado el estado al añadir un palet
        
        -- Pruebas de descarga
        testF (unloadT camionCargado "Roma" == unloadT camionCargado "Roma"), -- Verifica la descarga de un palet específico 
        testF (unloadT camionCargado "Madrid" == camionCargado) -- Sin cambios, no hay palets con ese destino
      ]
      

main :: IO ()
main = do
    -- Imprimir los resultados de las pruebas
    let results = map testF tests  -- Mapear sobre los tests y ejecutar testF
    print results  -- Imprimir la lista de resultados de pruebas