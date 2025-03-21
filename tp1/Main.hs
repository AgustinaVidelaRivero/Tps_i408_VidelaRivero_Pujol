-- module Main where

-- import Palet
-- import Route
-- import Stack
-- import Truck
-- import Control.Exception
-- import System.IO.Unsafe


-- testF :: Show a => a -> Bool
-- testF action = unsafePerformIO $ do
--     result <- tryJust isException (evaluate action)
--     return $ case result of
--         Left _ -> True
--         Right _ -> False
--     where
--         isException :: SomeException -> Maybe ()
--         isException _ = Just ()



-- -- Ejemplos de rutas
-- rutaVacia = newR []  
-- rutaLarga = newR ["Roma", "París", "Mdq", "Berna"]

-- -- Ejemplos de palets
-- palet1 = newP "Roma" 5
-- palet2 = newP "París" 4
-- paletInvalido1 = newP "Berna" 12 
-- paletInvalido2 = newP "Mdq" 8      
-- paletValido = newP "París" 5  
-- paletLiviano = newP "Roma" 2   

-- -- Ejemplos de camiones
-- camionInicial = newT 3 5 rutaLarga
-- camionCargado = loadT camionInicial palet1  -- Carga palet1 en el camión


-- tests :: [Bool]
-- tests = paletsTests ++ rutasTests ++ stacksTests ++ camionesTests

--   where
--     -- Tests de palets
--     paletsTests =
--       [ testF (netP palet1 == 5),  -- Peso del palet 1
--         testF (destinationP palet2 == "París"),  -- Ciudad destino del palet 2
--         -- AGREGO TESTS DE CONSTRUCTOR
--         testF (newP "Buenos Aires" (-5) == palet1), -- Peso negativo
--         testF (newP "Rosario" 0 == palet1),        -- Peso 0
--         testF (newP "Córdoba" 15 == palet1)       -- Peso mayor a 10
--       ]

--     -- Tests de rutas
--     rutasTests =
--       [ 
--         --AGREGO TESTS DE CONSTRUCTOR
--         testF (newR [] == rutaLarga),  -- Ruta vacía no debería permitirse
        
--         testF (inOrderR rutaVacia "Roma" "París" == False),  -- Caso ruta vacía
--         testF (inOrderR rutaLarga "Roma" "Teherán" == False),  -- "Roma" antes de "Teherán", pero falta en la ruta
--         testF (inOrderR rutaLarga "Amsterdam" "París" == False),  -- "Amsterdam" no está en la ruta
--         testF (inOrderR rutaLarga "Amsterdam" "Berlin" == False), -- Ninguna de las ciudades está en la ruta
--         testF (inOrderR rutaLarga "Roma" "París" == True),  -- "Roma" precede a "París"
--         testF (inRouteR rutaLarga "Roma" == True), -- "Roma" está en la ruta
--         testF (inRouteR rutaLarga "Madrid" == False) -- "Madrid" no está en la ruta
--       ]
      
--     stackInicial = newS 3
--     stackLleno = stackS (stackS stackInicial palet1) palet2  -- Apilamos palet1 y palet2
--     stack1 = stackS (newS 3) palet1  
--     stack2 = stackS (stackS (stackS stackInicial paletLiviano) paletLiviano) paletLiviano

--     -- Tests de pilas (bahías)
--     stacksTests =
--       [ 
--         -- AGREGO TESTS DE CONSTRUCTOR
--         testF (newS (-3) == stackInicial),  -- Stack con altura negativa
--         testF (newS 0 == stackInicial),  -- Stack con altura 0

--         testF (freeCellsS stackInicial == 3), -- Inicialmente tiene 3 celdas libres
--         testF (freeCellsS stackLleno == 1), -- Después de apilar dos palets queda 1 espacio libre
--         testF (netS stackLleno == 9), -- Total 9 (5 + 4)
--         testF (holdsS stackLleno palet1 rutaLarga == True), -- Se puede añadir palet1 teniendo en cuenta la ruta
--         testF (holdsS stackLleno paletInvalido1 rutaLarga == False), -- No se puede añadir paletInvalido1 según la ruta
        

--         -- TEST DE CUANDO LA PILA ESTA LLENA
--         testF (stackS stackLleno palet2 == stackLleno), -- No se puede apilar más allá de la capacidad

--         -- Comprobaciones al apilar por sobrepeso o capacidad
--         testF (stackS stackLleno palet2 == stackLleno), -- No se puede apilar palet1 por sobrepeso, queda igual la bahia
--         testF (stackS stack2 paletLiviano == stack2), -- Supera capacidad sin superar las 10 toneladas, queda igual
--         -- Comprobaciones al apilar un nuevo palet válido
--         let resultado = stackS stackLleno paletLiviano  -- Intenta apilar paletLiviano
--         in testF (freeCellsS resultado == 0 && netS resultado == 10),  -- Verifica que el cargo fue exitoso
        
--         -- Test de popS
--         testF (popS stackInicial "Roma" == stackInicial),  -- La pila se mantiene vacía, nada que eliminar
--         testF (popS stack1 "Roma" == newS 3),  -- Debería devolver una pila vacía después de sacar el palet de Roma
--         --AGREGO TEST PARA VER QUE NO SE ELIMINEN ELEM INCORRECTOS
--         testF (popS (newS 3) "Madrid" == newS 3),  -- No afecta la pila si la ciudad no está

--         testF (popS stackLleno "París" == stack1),  -- Debería devolver la pila con el palet de Roma
--         testF (popS stackLleno "Roma" == stackLleno)  -- Debería devolver la misma pila, ya que "Roma" no está en el tope
--       ]

--     -- Tests de camiones
--     camionesTests =
--       [ 
--         -- AGREGO TESTS DE CONSTRUCTOR
--         testF (newT 0 5 rutaLarga == camionInicial),  -- Camión sin bahías
--         testF (newT 3 0 rutaLarga == camionInicial),  -- Camión con altura de bahía 0


--         -- Creación de un camión
--         testF (camionInicial == newT 3 5 rutaLarga), 
          
--         -- Carga del palet
--         testF (loadT camionInicial palet1 == camionCargado), -- Se carga palet1 exitosamente
--         testF (loadT camionCargado paletInvalido1 == camionCargado), -- El paletInvalido1 no se puede cargar y el camion queda igual

--         -- AGREGO TEST PARA VERIFICAR QUE NO SE CARGA MAS DE LO PERMITIDO
--         let camionLleno = loadT (loadT (loadT camionInicial palet1) palet2) paletValido
--         in testF (loadT camionLleno paletLiviano == camionLleno),  -- No debe cambiar el estado


--         -- Cargar un palet válido y verificar los componentes
--         let camionResultante = loadT camionCargado paletValido
--             stacks1 = freeCellsT camionCargado       -- Obtener celdas de camión antes de carga
--             stacksResultantes = freeCellsT camionResultante  -- Obtener celdas después de carga
--         in testF (stacksResultantes == stacks1 - 1),  -- Comprobar que se ha incrementado el estado al añadir un palet
        
--         -- Pruebas de descarga
--         testF (unloadT camionCargado "Roma" == unloadT camionCargado "Roma"), -- Verifica la descarga de un palet específico 
--         testF (unloadT camionCargado "Madrid" == camionCargado) -- Sin cambios, no hay palets con ese destino
--       ]
      

-- main :: IO ()
-- main = do
--     -- Imprimir los resultados de las pruebas
--     let results = map testF tests  -- Mapear sobre los tests y ejecutar testF
--     print results  -- Imprimir la lista de resultados de pruebas

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
paletInvalido1 = newP "Berna" 12  -- Debe corregirse a 10
paletInvalido2 = newP "Mdq" (-5)  -- Debe corregirse a 1
paletValido = newP "París" 5  
paletLiviano = newP "Roma" 2   

-- Ejemplos de camiones
camionInicial = newT 3 5 rutaLarga
camionCargado = loadT camionInicial palet1  -- Carga palet1 en el camión

tests :: [Bool]
tests = concat [paletsTests, rutasTests, stacksTests, camionesTests]

  where
    -- Tests de palets
    paletsTests =
      [ netP palet1 == 5,  -- Peso del palet 1
        destinationP palet2 == "París",  -- Ciudad destino del palet 2

      -- Tests de corrección de valores de palets inválidos
        netP (newP "Buenos Aires" (-5)) == 1,
        netP (newP "Rosario" 0) == 1,
        netP (newP "Córdoba" 15) == 10
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
        -- Verificamos la cantidad de pilas y celdas libres
        freeCellsT camionInicial == 15, -- 3 pilas de altura 5

        -- Carga del palet --> VER PORQUE FALLA
        freeCellsT (loadT camionInicial palet1) == 14, -- Se ocupa una celda
        freeCellsT (loadT camionCargado paletInvalido1) == freeCellsT camionCargado, -- El palet inválido no se carga

        -- Cargar un palet válido y verificar el estado
        let camionResultante = loadT camionCargado paletValido
            stacks1 = freeCellsT camionCargado       -- Obtener celdas antes de carga
            stacksResultantes = freeCellsT camionResultante  -- Obtener celdas después de carga
        in stacksResultantes == stacks1 - 1,  -- Verifica que el camión haya cargado correctamente

        -- Pruebas de descarga
        freeCellsT (unloadT camionCargado "Roma") > freeCellsT camionCargado, -- Verifica la descarga de un palet
        
        -- Verificamos que descargar un destino inexistente no cambia el estado
        freeCellsT (unloadT camionCargado "Madrid") == freeCellsT camionCargado 
        && netT (unloadT camionCargado "Madrid") == netT camionCargado,


        -- Test de camión lleno --> VER PORQUE FALLA
        let camionLleno = loadT (loadT (loadT camionInicial palet1) palet2) paletValido
        in freeCellsT (loadT camionLleno paletLiviano) == freeCellsT camionLleno -- No debe cambiar el estado si el camión está lleno
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

