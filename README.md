## Brujula

The purpose of this application is to show the proper implementation of Brujula Library

## Brujula SDK

Brujula SDK has a functionality to Situm SDK and GPS

### Integration via AAR file

1. Download latest library *.aar file from repository.
2. Put aar file into your project libs folder
3. In your settings.gradle file add this

```
...
repositories {
        ...
        flatDir {
            dirs 'libs'
        }

}
...
```

4. In your module-level build.gradle add the following line

```
implementation(name:'brujula-release-<VERSION>', ext:'aar')
```

### Implementation

```
class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Brujula.Companion.Builder(
            context = this,
            userId = userId,
            fullName = fullName,
            apiToken = apiKey
        ).build()
    }
}
```

### Setup listener

Add listener to receive entered area and prize

```
Brujula.getInstance().addOnBrujulaListener(object : OnBrujulaListener {
            override fun onEnterArea(area: Area) {
                //Area
            }

            override fun onPrizeWin(prize: Prize) {
                //Prize 
            }
        })
```

### Nearest area

You can receive nearest areas by triggering this method

```
coroutineScope.launch {
                val areas = Brujula.getInstance().getNearestAreas()
                ...
            }
```

### Map

Create a BrujulaMapView in your xml layout:

```
<co.leveltech.brujula.view.BrujulaMapView
        android:id="@+id/brujulaMapView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
```

Call method to configure BrujulaMapView

```
val mapView = view.findViewById<BrujulaMapView>(R.id.brujulaMapView)
Brujula.getInstance().configureMapView(mapView)
```

