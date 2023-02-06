Diplomado Desarrollo de Aplicaciones para Dispositivos Móviles - Modulo 6 - Proyecto 2
===========

Alexei E. Martínez Mendoza 

Pequeño proyecto de una biblioteca y que muestra los libros que contiene.

## Estructura del proyecto

Antes de correr el proyecto colocar las llaves y credenciales para usar los servicios de Firebase y de Google Maps

1. Clonar el proyecto
```bash
git clone git@github.com:alexeiemmanuel/biblioteca_apiary.git

cd biblioteca_apiary
```
2. Para Firebase colocar el archivo *google-services.json* en biblioteca_apiary/app
3. Para Google Maps colocar el archivo *google_maps_apikey.xml* en biblioteca_apiary/app/src/main/res/values
4. Sincronizar el proyecto para que baje las dependencias del proyecto
5. Correr el proyecto

```bash
# Comando para ver la estructura de un directorio
tree -I "build|test|debug|androidTest|*.md"

├── app
│   ├── build.gradle
│   ├── google-services.json     # Colocar las credenciales de Firebase con el mismo nombre en formato .json
│   ├── libs
│   ├── proguard-rules.pro
│   └── src
│       └── main
│           ├── AndroidManifest.xml
│           ├── java
│           │   └── com
│           │       └── aemm
│           │           └── libreria
│           │               ├── model
│           │               │   ├── BookDetail.kt
│           │               │   └── Book.kt
│           │               ├── network
│           │               │   ├── BooksApi.kt
│           │               │   └── RetrofitService.kt
│           │               ├── util
│           │               │   ├── Constants.kt
│           │               │   └── EnumFirebaseCode.kt
│           │               └── view
│           │                   ├── activities
│           │                   │   ├── LibraryActivity.kt
│           │                   │   ├── LoginActivity.kt
│           │                   │   └── SplashScreenActivity.kt
│           │                   ├── adapters
│           │                   │   └── BookAdapter.kt
│           │                   └── fragments
│           │                       ├── BookDetailFragment.kt
│           │                       ├── BookMapFragment.kt
│           │                       └── BooksListFragment.kt
│           └── res
│               ├── drawable
│               ├── drawable-v24
│               ├── layout
│               ├── mipmap-anydpi-v26
│               ├── mipmap-hdpi
│               ├── mipmap-mdpi
│               ├── mipmap-xhdpi
│               ├── mipmap-xxhdpi
│               ├── mipmap-xxxhdpi
│               ├── navigation
│               ├── values
│               │   ├── colors.xml
│               │   ├── dimens.xml
│               │   ├── dimen.xml
│               │   ├── google_maps_apikey.xml   # Colocar la Key de google maps con el mismo nombre en formato .xml
│               │   ├── strings.xml
│               │   ├── styles.xml
│               │   └── themes.xml
│               ├── values-night
│               └── xml
├── build.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradle.properties
├── gradlew
├── gradlew.bat
├── local.properties
└── settings.gradle

```

```
# URL Apiary que contiene los Endpoints
https://private-1768dd-library48.apiary-mock.com/api/v1/
```

`Endpoint que devuelve un listado de libros https://private-1768dd-library48.apiary-mock.com/api/v1/books`
```json
[
    {
        "id": "1",
        "title": "Alicia en el país de las maravillas",
        "release": "02 Mar 2016",
        "synopsis": "Novela que juega con la lógica. Alicia cae por un agujero, encontrándose un mundo peculiar. Alicia es una niña a la que no le gusta leer libros sin dibujos. Un día se encuentra con su hermana a la orilla de un río, aburrida y de repente ve cómo un conejo blanco revisa su reloj y exclama que llega tarde. ¿De dónde viene el conejo y por qué llega tarde? Movida por la curiosidad Alicia lo sigue y llega a un mundo subterráneo lleno de maravillas, donde vive miles de aventuras que rozan lo irracional, donde las situaciones son como en los sueños y los animales hablan.",
        "thumbnail": "https://covers.alibrate.com/b/59872e85cba2bce50c19bdea/9e39e39f-6a5e-4171-bd1d-4e791d13cfbc/medium"
    },
    {
        "id": "2",
        "title": "Ben Hur",
        "release": "07 Abr 2018",
        "synopsis": "La historia de un príncipe judío ficticio, Judá Ben-Hur y sus peripecias en la época de Jesucristo en un mundo en el que se gestaba una nueva fe. a trama básica se desarrolla entorno a la vida de Judá Ben-Hur, un coetáneo y compatriota del Nazareno. Pese a su amistad con el romano Messala, el antagonismo entre estos personajes y un desafortunado accidente llevan a la acaudalada familia de los Hur a la desgracia. El rico judío fue hecho galeote en tanto que la situación de su madre y hermana queda ignorada. El destino hace que, tras una batalla contra los piratas, Ben-Hur se convierta en el hijo adoptivo del noble romano Quinto Arrio. Sin embargo, un objetivo secreto lo devuelve a Oriente, a Antioquía, donde se cruzarán de nuevo los caminos. Con la ayuda del viejo Simónides y el jeque árabe Ilderim, se enfrenta a Messala y consigue su venganza en una carrera de cuádrigas. Después se dirigen a Jerusalén, donde se esperaba la aparición del Mesías.",
        "thumbnail": "https://covers.alibrate.com/b/59872e88cba2bce50c1a1dd8/b0a64acd-657d-4918-b82e-34716b612b2e/medium"
    }
]
```

`Endpoint que devuelve los detalles de un libro por su id libros https://private-1768dd-library48.apiary-mock.com/api/v1/book/2`
```json
{
  "id": "2",
  "title": "Ben Hur",
  "author":"Lew Wallace",
  "edition": "3",
  "isbn": "9789177596202",
  "genre": "Clásicos universales",
  "synopsis": "La historia de un príncipe judío ficticio, Judá Ben-Hur y sus peripecias en la época de Jesucristo en un mundo en el que se gestaba una nueva fe. a trama básica se desarrolla entorno a la vida de Judá Ben-Hur, un coetáneo y compatriota del Nazareno. Pese a su amistad con el romano Messala, el antagonismo entre estos personajes y un desafortunado accidente llevan a la acaudalada familia de los Hur a la desgracia. El rico judío fue hecho galeote en tanto que la situación de su madre y hermana queda ignorada. El destino hace que, tras una batalla contra los piratas, Ben-Hur se convierta en el hijo adoptivo del noble romano Quinto Arrio. Sin embargo, un objetivo secreto lo devuelve a Oriente, a Antioquía, donde se cruzarán de nuevo los caminos. Con la ayuda del viejo Simónides y el jeque árabe Ilderim, se enfrenta a Messala y consigue su venganza en una carrera de cuádrigas. Después se dirigen a Jerusalén, donde se esperaba la aparición del Mesías.",
  "release": "07 Abr 2018",
  "thumbnail": "https://covers.alibrate.com/b/59872e88cba2bce50c1a1dd8/b0a64acd-657d-4918-b82e-34716b612b2e/medium",
  "editorial": {
    "name":  "Porrua",
    "schedule": "Lunes a Viernes 10:00 a 20:00 hrs.\n Sábado 10:00 a 15:00",
    "telephone": "55 2378 2309",
    "latitude":  19.530778631378137,
    "longitude": -99.02850035563819
  }
}
```
