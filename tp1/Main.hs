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
rutaLarga = newR ["Roma", "París", "Mdq", "Berna"]

-- Ejemplos de palets
palet1 = newP "Roma" 5
palet2 = newP "París" 4 
paletInvalido1 = newP "Berna" 1
paletValido = newP "París" 5  
paletLiviano = newP "Roma" 2   
paletLiviano2 = newP "Roma" 1

-- Ejemplos de camiones
camionInicial = newT 3 5 rutaLarga -- 3 bahias con altura 5
camionChico = newT 1 3 rutaLarga  -- 1 bahia con altura 3
camionCargado = loadT camionInicial palet1  
camionCasiLleno = loadT (loadT camionChico palet2) palet1
camionLleno = loadT (loadT (loadT camionChico palet2) palet1) paletLiviano2    -- este ultimo palet no se esta cargando NOSE PORQUE

tests :: [Bool]
tests = concat [paletsTests, rutasTests, stacksTests, camionesTests]

  where
    -- Tests de palets
    paletsTests =
      [ netP palet1 == 5,  -- Peso del palet 1
        destinationP palet2 == "París",  -- Ciudad destino del palet 2

    -- Tests en los que los palets con peso invalido deben tirar error 
        testF (newP "Buenos Aires" (-5)),
        testF (newP "Rosario" 0),
        testF (newP "Córdoba" 15)
      ]

    -- Tests de rutas
    rutasTests =
      [ not (inOrderR rutaLarga "Roma" "Teherán"),  -- "Roma" antes de "Teherán", pero falta en la ruta
        not (inOrderR rutaLarga "Amsterdam" "París"),  -- "Amsterdam" no está en la ruta
        inOrderR rutaLarga "Roma" "París",  -- "Roma" precede a "París"
        inRouteR rutaLarga "Roma", -- "Roma" está en la ruta
        not (inRouteR rutaLarga "Madrid"), -- "Madrid" no está en la ruta

        -- Test de errores en rutas
        testF (newR [])  -- Ruta vacía debería fallar
      ]
      
    stackInicial = newS 3
    stackLleno = stackS (stackS stackInicial palet1) palet2  -- Apilamos palet1 y palet2
    stack1 = stackS (newS 3) palet1  
    stack2 = stackS (stackS (stackS stackInicial paletLiviano) paletLiviano) paletLiviano

    -- Tests de pilas (stacks)
    stacksTests =
      [ freeCellsS stackInicial == 3, -- Inicialmente tiene 3 celdas libres
        freeCellsS stackLleno == 1, -- Después de apilar dos palets queda 1 espacio libre
        netS stackLleno == 9, -- Total 9 (5 + 4)
        holdsS stackLleno palet1 rutaLarga, -- Se puede añadir palet1 según la ruta
        not (holdsS stackLleno paletInvalido1 rutaLarga), -- No se puede añadir paletInvalido1 según la ruta

        -- Verificar que no se pueda sobrecargar el stack

        freeCellsS (stackS stackLleno palet2) == freeCellsS stackLleno, -- No cambia si está lleno


        -- Test de popS en una pila vacía
        freeCellsS (popS (newS 3) "Madrid") == 3  -- No afecta la pila si la ciudad no está
      ]

    -- Tests de camiones
    camionesTests =
      [ 
        -- Verificamos la cantidad de bahías y celdas libres
        freeCellsT camionInicial == 15, -- 3 bahías  de altura 5
        freeCellsT camionCasiLleno == 1,
        freeCellsT camionLleno == 0,   

        -- Cargas del palets 
        freeCellsT (loadT camionInicial palet1) == 14, -- Se ocupa una celda
        freeCellsT (loadT camionCasiLleno palet1) == freeCellsT (camionCasiLleno), -- El palet1 no se carga por sobrepeso en la bahia
        freeCellsT (loadT camionCasiLleno palet2) == freeCellsT (camionCasiLleno), -- El palet2 no se carga por ruta invalida
        freeCellsT (loadT camionLleno paletLiviano2) == freeCellsT (camionLleno), -- El palet Liviano no puede ser cargado porque no hay mas espacio en el camion

        -- Cargar un palet válido y verificar el estado
        let camionResultante = loadT camionCargado paletValido
            stacks1 = freeCellsT camionCargado       -- Obtener celdas antes de carga
            stacksResultantes = freeCellsT camionResultante  -- Obtener celdas después de carga
        in stacksResultantes == stacks1 - 1,  -- Verifica que el camión haya cargado correctamente

        -- Pruebas de descarga
        freeCellsT (unloadT camionCargado "Roma") > freeCellsT camionCargado, -- Verifica la descarga de un palet
        freeCellsT (unloadT camionLleno "Roma") == 2,  -- Se descargan varios palets en la misma ciudad destino
        
        -- Verificamos que descargar un destino inexistente no cambia el estado
        freeCellsT (unloadT camionCargado "Madrid") == freeCellsT camionCargado 
        && netT (unloadT camionCargado "Madrid") == netT camionCargado
      ]

-- Función para imprimir los tests fallidos
main :: IO ()
main = do
    let indexedTests = zip [1..] tests
    let failedTests = filter (not . snd) indexedTests
    print failedTests
    if null failedTests
        then putStrLn "Todos los tests pasaron correctamente."
        else putStrLn "Algunos tests fallaron."

    print tests

