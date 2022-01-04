package koala.tea.sceneformhelp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.Scene
import com.google.ar.sceneform.SceneView
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.Color
import com.google.ar.sceneform.rendering.MaterialFactory
import com.google.ar.sceneform.rendering.ShapeFactory
import com.google.ar.sceneform.ux.FootprintSelectionVisualizer
import com.google.ar.sceneform.ux.TransformableNode
import com.google.ar.sceneform.ux.TransformationSystem

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val scene = findViewById<SceneView>(R.id.scene)

        val ts = TransformationSystem(
            resources.displayMetrics,
            FootprintSelectionVisualizer()
        )
        val transformNode = TransformableNode(ts)
        transformNode.parent = scene.scene
        transformNode.worldPosition = Vector3(0f, 0f, -0.5f)
        transformNode.select()

        val modelNode = Node()
        modelNode.parent = transformNode

        MaterialFactory.makeTransparentWithColor(
            application,
            Color(android.graphics.Color.GRAY)
        )
            .thenAccept { material ->
                material.setFloat(MaterialFactory.MATERIAL_ROUGHNESS, 10f)
                modelNode.renderable = ShapeFactory.makeCube(Vector3(0.3f, 0.3f, 0.3f), Vector3.zero(), material)
            }

        val listener =
            Scene.OnPeekTouchListener { hitTestResult, motionEvent ->
                try {
                    ts.onTouch(hitTestResult, motionEvent)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        scene.scene.addOnPeekTouchListener(listener)
    }

    override fun onResume() {
        super.onResume()

        findViewById<SceneView>(R.id.scene).resume()
    }

    override fun onPause() {
        super.onPause()

        findViewById<SceneView>(R.id.scene).pause()
    }
}