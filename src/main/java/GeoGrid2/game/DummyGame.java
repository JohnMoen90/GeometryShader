package GeoGrid2.game;

import GeoGrid2.engine.GridItem;
import GeoGrid2.engine.graph.MeshUtils;
import org.joml.Vector3f;
import GeoGrid2.engine.IGameLogic;
import GeoGrid2.engine.Window;
import GeoGrid2.engine.graph.Mesh;

import static org.lwjgl.glfw.GLFW.*;

public class DummyGame implements IGameLogic {

    private final DataTransfer dt;

    private int displxInc = 0;

    private int displyInc = 0;

    private int displzInc = 0;

    private int scaleInc = 0;

    private MeshUtils meshUtils;

    int numTilesX;
    int numTilesY;

    float tileSize;

    private final Renderer renderer;

    private GridItem[] gridItems;

    public DummyGame(DataTransfer dt) {
        renderer = new Renderer();
        this.dt = dt;
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);

        numTilesX = 3601;
        numTilesY = 3601;

        meshUtils = new MeshUtils(numTilesX, numTilesY);

        tileSize = (float) 2 / numTilesX;

        // Create the Mesh
//        float[] positions = new float[]{
//                -0.5f,  0.5f, // top-left
//                0.5f,  0.5f, // top-right
//                0.5f, -0.5f, // bottom-right
//                -0.5f, -0.5f  // bottom-left
//        };

        float[] positions = meshUtils.createVertexArray();
        float[] colours = meshUtils.createColorArray();
        int[] indices = meshUtils.createIndiceArray();

        Mesh mesh = new Mesh(positions, colours, indices, tileSize);
        GridItem gridItem = new GridItem(mesh);
        gridItem.setPosition(0, 0, -2);
        gridItems = new GridItem[] {gridItem};
    }


    @Override
    public void input(Window window) {
        displyInc = 0;
        displxInc = 0;
        displzInc = 0;
        scaleInc = 0;
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            displyInc = 1;
        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            displyInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            displxInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            displxInc = 1;
        } else if (window.isKeyPressed(GLFW_KEY_A)) {
            displzInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_Q)) {
            displzInc = 1;
        } else if (window.isKeyPressed(GLFW_KEY_Z)) {
            scaleInc = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            scaleInc = 1;
        }
    }

    @Override
    public void update(float interval) {




        for (GridItem gridItem : gridItems) {
            float scale = gridItem.getScale();
            // Update position
            Vector3f itemPos = gridItem.getPosition();
            float posx = itemPos.x + displxInc * (scale * 0.01f);
            float posy = itemPos.y + displyInc * (scale * 0.01f);
            float posz = itemPos.z + displzInc * (scale * 0.01f);
            gridItem.setPosition(posx, posy, posz);
            
            // Update scale
            scale += scaleInc * (scale * 0.05f);
            if ( scale < 0 ) {
                scale = 0;
            }
            gridItem.setScale(scale);
            
            // Update rotation angle
//            float rotation = gridItem.getRotation().z + 1.5f;
//            if ( rotation > 360 ) {
//                rotation = 0;
//            }
//            gridItem.setRotation(0, 0, rotation);
        }
    }

    @Override
    public void render(Window window) {
        renderer.render(window, gridItems);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for (GridItem gridItem : gridItems) {
            gridItem.getMesh().cleanUp();
        }
    }

}