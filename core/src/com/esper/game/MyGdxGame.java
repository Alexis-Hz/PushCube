/*
/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.esper.game;

        import com.badlogic.gdx.*;
        import com.badlogic.gdx.assets.AssetManager;
        import com.badlogic.gdx.assets.loaders.ModelLoader;
        import com.badlogic.gdx.files.FileHandle;
        import com.badlogic.gdx.graphics.*;
        import com.badlogic.gdx.graphics.VertexAttributes.Usage;
        import com.badlogic.gdx.graphics.g2d.Batch;
        import com.badlogic.gdx.graphics.g2d.BitmapFont;
        import com.badlogic.gdx.graphics.g3d.Environment;
        import com.badlogic.gdx.graphics.g3d.Model;
        import com.badlogic.gdx.graphics.g3d.ModelBatch;
        import com.badlogic.gdx.graphics.g3d.ModelInstance;
        import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
        import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
        import com.badlogic.gdx.graphics.g3d.Material;
        import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
        import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
        import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
        import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
        import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
        import com.badlogic.gdx.math.Intersector;
        import com.badlogic.gdx.math.Vector3;
        import com.badlogic.gdx.math.collision.BoundingBox;
        import com.badlogic.gdx.math.collision.Ray;
        import com.badlogic.gdx.scenes.scene2d.Actor;
        import com.badlogic.gdx.scenes.scene2d.Stage;
        import com.badlogic.gdx.scenes.scene2d.ui.Label;
        import com.badlogic.gdx.scenes.scene2d.ui.Skin;
        import com.badlogic.gdx.scenes.scene2d.utils.Align;
        import com.badlogic.gdx.utils.Array;

        import com.badlogic.gdx.utils.Json;
        import com.badlogic.gdx.utils.JsonWriter;
        import com.esper.game.Block;
        import com.esper.game.Cube;

        import java.util.Random;

/**
 * See: http://blog.xoppa.com/basic-3d-using-libgdx-2/
 * @author Xoppa
 */
public class MyGdxGame extends InputAdapter implements ApplicationListener {
    public static class GameObject extends ModelInstance {
        public final Vector3 center = new Vector3();
        public final Vector3 dimensions = new Vector3();
        //public int idX, idY, idZ;
        public Block blockref;
        public int face;
        //0 : up
        //1 : bottom
        //2 : left-front
        //3 : right-front
        //4 : left-back
        //5 : right-back
        final static BoundingBox bounds = new BoundingBox();
        public final float radius;
        public GameObject(Model model, Block b, int tface) {
            super(model);
            calculateBoundingBox(bounds);
            center.set(bounds.getCenter());
            dimensions.set(bounds.getDimensions());
            radius = dimensions.len() / 2f;
            blockref =b;
            face = tface;
            //idX = idXX;
            //idY = idYY;
            //idZ = idZZ;
        }
        public BoundingBox getTranslatedBoundingBox()
        {
            return bounds.mul(this.transform);
        }
    }
    public class UndoButton extends Actor {
        Texture texture = new Texture(Gdx.files.internal("textures/undo2.png"));
        int xDraw = 0;
        int yDraw = 0;
        public void draw(Batch batch, float alpha){
            batch.draw(texture,xDraw,yDraw);
        }
        public void setDraw(int x, int y)
        {
            xDraw = x;
            yDraw = y;
        }
    }
    public class BackButton extends Actor {
        Texture texture = new Texture(Gdx.files.internal("textures/back.png"));
        int xDraw = 0;
        int yDraw = 0;
        public void draw(Batch batch, float alpha){
            batch.draw(texture,xDraw,yDraw);
        }
        public void setDraw(int x, int y)
        {
            xDraw = x;
            yDraw = y;
        }
    }
    public class LevelCompleteImage extends Actor {
        Texture texture = new Texture(Gdx.files.internal("textures/level_complete.png"));
        int xDraw = 0;
        int yDraw = 0;
        public void draw(Batch batch, float alpha){
            batch.draw(texture,xDraw,yDraw);
        }
        public void setDraw(int x, int y)
        {
            xDraw = x;
            yDraw = y;
        }
    }
    public class StarImage extends Actor {
        Texture texture = new Texture(Gdx.files.internal("textures/GoldStar.png"));
        int xDraw = 0;
        int yDraw = 0;
        public void draw(Batch batch, float alpha){
            batch.draw(texture,xDraw,yDraw);
        }
        public void setDraw(int x, int y)
        {
            xDraw = x;
            yDraw = y;
        }
    }
    public class TitleImage extends Actor {
        Texture texture = new Texture(Gdx.files.internal("images/title.png"));
        int xDraw = 0;
        int yDraw = 0;
        public void draw(Batch batch, float alpha){
            batch.draw(texture,xDraw,yDraw);
        }
        public void setDraw(int x, int y)
        {
            xDraw = x;
            yDraw = y;
        }
    }
    public class NewGameImage extends Actor {
        Texture texture = new Texture(Gdx.files.internal("images/new_game.png"));
        int xDraw = 0;
        int yDraw = 0;
        public void draw(Batch batch, float alpha){
            batch.draw(texture,xDraw,yDraw);
        }
        public void setDraw(int x, int y)
        {
            xDraw = x;
            yDraw = y;
        }
    }
    public class ContinueImage extends Actor {
        Texture texture = new Texture(Gdx.files.internal("images/continue.png"));
        int xDraw = 0;
        int yDraw = 0;
        public void draw(Batch batch, float alpha){
            batch.draw(texture,xDraw,yDraw);
        }
        public void setDraw(int x, int y)
        {
            xDraw = x;
            yDraw = y;
        }
    }
    public class QuitImage extends Actor {
        Texture texture = new Texture(Gdx.files.internal("images/quit_game.png"));
        int xDraw = 0;
        int yDraw = 0;
        public void draw(Batch batch, float alpha){
            batch.draw(texture,xDraw,yDraw);
        }
        public void setDraw(int x, int y)
        {
            xDraw = x;
            yDraw = y;
        }
    }

    public Environment environment;
    public PerspectiveCamera cam;
    public CameraInputController camController;
    public ModelBatch modelBatch;
    public Model model;
    public Model model2;
    public GameObject instance;
    public GameObject instance2;

    public boolean loading;
    public AssetManager assets;
    public Array<GameObject> instances = new Array<GameObject>();

    Cube lexCube;

    protected Stage stage;
    protected Label label;
    protected Label parLabel;
    protected Label movesLabel;
    protected BitmapFont font;
    protected StringBuilder stringBuilder;

    private int selected = -1, selecting = -1;
    private GameObject selectedCube;
    private Material selectionMaterial;
    private Material originalMaterial;
    private String debugText = "";
    private String solvedText = "";

    UndoButton undoButton;
    BackButton backButton;
    LevelCompleteImage levelCompleteImage;
    StarImage starImage;
    TitleImage titleImage;
    NewGameImage newGameImage;
    ContinueImage continueImage;
    QuitImage quitImage;

    int cubeSize = 2;
    int turnCount = 2;
    int level = 1;


    private int visibleCount = 0;
    int screen = 0;
    boolean levelComplete = false;
    protected Label levelCompleteLabel;
    int lastUnlocked = 0;
    boolean firstTurn = true;
    public int moves = 0;
    //SaveFile saveFile;

    public void create() {
        //saveFile = new SaveFile();
        //save(saveFile);
        loadState();

        stage = new Stage();
        undoButton = new UndoButton();
        stage.addActor(undoButton);

        backButton = new BackButton();
        stage.addActor(backButton);

        levelCompleteImage = new LevelCompleteImage();
        stage.addActor(levelCompleteImage);

        starImage = new StarImage();
        stage.addActor(starImage);

        titleImage = new TitleImage();
        stage.addActor(titleImage);

        newGameImage = new NewGameImage();
        stage.addActor(newGameImage);

        continueImage = new ContinueImage();
        stage.addActor(continueImage);

        quitImage = new QuitImage();
        stage.addActor(quitImage);
        //last two lines are new

        font = new BitmapFont();
        label = new Label(" ", new Label.LabelStyle(font, Color.WHITE));

        levelCompleteLabel = new Label(" ", new Label.LabelStyle(font, Color.WHITE));
        stage.addActor(label);

        parLabel = new Label(" ", new Label.LabelStyle(font, Color.WHITE));
        stage.addActor(parLabel);

        movesLabel = new Label(" ", new Label.LabelStyle(font, Color.WHITE));
        stage.addActor(movesLabel);

        stringBuilder = new StringBuilder();

        lexCube =  new Cube(cubeSize);

        lexCube.turn(turnCount);


        //lexCube.deform(0,0,0,1,0,0);
        //lexCube.deform(0,0,0,1,0,0);
        //lexCube.cube[0][0][0].push(-1,0,0);

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        modelBatch = new ModelBatch();

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(7f, 7f, 7f);
        cam.lookAt(0,0,0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        camController = new CameraInputController(cam);
        //Gdx.input.setInputProcessor(camController);
        Gdx.input.setInputProcessor(new InputMultiplexer(this, camController));
        Gdx.gl20.glEnable(GL20.GL_CULL_FACE);//culling
        Gdx.gl20.glCullFace(GL20.GL_BACK);


        assets =  new AssetManager();
        assets.load("ship.obj", Model.class);
        assets.load("spacesphere.obj", Model.class);
        loading = true;

        selectionMaterial = new Material();
        selectionMaterial.set(ColorAttribute.createDiffuse(Color.ORANGE));
        originalMaterial = new Material();
    }
    //private FileHandle file =  Gdx.files.local("bin/save_file.json");
    Preferences prefs;
    public void loadState()
    {
        prefs = Gdx.app.getPreferences("My Preferences");
        int lastLevel =  prefs.getInteger("last_level");
        System.out.println("last unlocked: " + prefs.getInteger("last_unlocked"));
        if(lastLevel == 0)//there is no preference
        {
            prefs.putInteger("last_level", 1);
            prefs.putInteger("last_unlocked", 1);
            prefs.flush();
        }
        System.out.println("last unlocked: " + prefs.getInteger("last_unlocked"));

        level = prefs.getInteger("last_level");
        lastUnlocked = prefs.getInteger("last_unlocked");

    }
    public void saveState()
    {
        if(level > lastUnlocked)
        {
            prefs.putInteger("last_unlocked", level);
        }
        prefs.putInteger("last_level", level);
        prefs.flush();
    }
    public void levelUpCube()
    {
        level ++;
        /*
        turnCount += 2;
        if (turnCount > cubeSize*cubeSize*cubeSize)
        {
            cubeSize ++;
            turnCount = cubeSize;
        }
        */
        resetCube();
        saveState();
    }
    public void resetSize()
    {
        int i = 0;
        if(level < 4)// two
        {
            cubeSize = 2;
        }
        else if(level < 13)//three
        {
            cubeSize = 3;
        }
        else if(level < 26)//four
        {
            cubeSize = 4;
        }
        else //five
        {
            cubeSize = 5;
        }
        //cubeSize = ((level)/5 )+2;
    }
    public void resetTurnCount()
    {
        if(level < 4)// two
        {
            turnCount = level+1;
        }
        else if(level < 13)//three
        {
            turnCount = level-4+1;
        }
        else if(level < 26)//four
        {
            turnCount = level-13+1;
        }
        else //five
        {
            turnCount = level-26+1;
        }

    }
    public void resetCube()
    {
        resetSize();
        resetTurnCount();
        lexCube =  new Cube(cubeSize);
        lexCube.turn(turnCount);
        instances = new Array<GameObject>();
        loading = true;
        firstTurn = true;
        moves = 0;
    }
    /*
    private Model createPlaneModel(final float width, final float height, final Material material,
                                   final float u1, final float v1, final float u2, final float v2) {

        modelBuilder.begin();
        MeshPartBuilder bPartBuilder = modelBuilder.part("rect",
                GL20.GL_TRIANGLES, Usage.Position | Usage.Normal | Usage.TextureCoordinates,
                material);
//NOTE ON TEXTURE REGION, MAY FILL OTHER REGIONS, USE GET region.getU() and so on
        bPartBuilder.setUVRange(u1, v1, u2, v2);
        bPartBuilder.rect(
                -(width*0.5f), -(height*0.5f), 0,
                (width*0.5f), -(height*0.5f), 0,
                (width*0.5f), (height*0.5f), 0,
                -(width*0.5f), (height*0.5f), 0,
                0, 0, -1);


        return (modelBuilder.end());
    }
    */

    public ModelInstance space;
    ModelBuilder modelBuilder;
    private void doneLoading() {
        Model ship = assets.get("ship.obj", Model.class);
        for (int i = 0 ; i < lexCube.sideLenght; i++)
        {
            for (int j =0; j < lexCube.sideLenght; j++)
            {
                for(int k = 0; k < lexCube.sideLenght; k++)
                {
                    modelBuilder = new ModelBuilder();
                    model = modelBuilder.createBox(1f, 1f, 1f, new Material(ColorAttribute.createDiffuse(Color.DARK_GRAY)), Usage.Position | Usage.Normal);

                    instance = new GameObject(model, lexCube.cube[i][j][k], -1);//cube

                    instances.add(instance);

                    Random rand = new Random();
                    int colorA = rand.nextInt();
                    int colorB = rand.nextInt();
                    int colorC = rand.nextInt();
                    //cube [i][j][k] = new Block(i, j, k);
                    //ModelBuilder modelBuilder = new ModelBuilder();
                    modelBuilder = new ModelBuilder();
                    model = modelBuilder.createBox(.8f, .01f, .8f, new Material(ColorAttribute.createDiffuse(Color.DARK_GRAY)), Usage.Position | Usage.Normal);

                    instance = new GameObject(model, lexCube.cube[i][j][k], 0);//top face
                    //Random rand = new Random();
                    if(lexCube.cube[i][j][k].faces[0] ==  1)
                        instance.materials.get(0).set(ColorAttribute.createDiffuse(Color.WHITE));//colorize

                    instances.add(instance);


                    modelBuilder = new ModelBuilder();
                    model = modelBuilder.createBox(.8f, .01f, .8f,
                            new Material(ColorAttribute.createDiffuse(Color.DARK_GRAY)),
                            Usage.Position | Usage.Normal);
                    instance = new GameObject(model, lexCube.cube[i][j][k], 1);//bottom face;
                    //rand = new Random();
                    if(lexCube.cube[i][j][k].faces[1] ==  1)
                        instance.materials.get(0).set(ColorAttribute.createDiffuse(Color.YELLOW));//colorize

                    instances.add(instance);

                    modelBuilder = new ModelBuilder();
                    model = modelBuilder.createBox(.8f, .8f, .01f,
                            new Material(ColorAttribute.createDiffuse(Color.DARK_GRAY)),
                            Usage.Position | Usage.Normal);
                    instance = new GameObject(model, lexCube.cube[i][j][k], 2);//front-left face;
                    //rand = new Random();
                    if(lexCube.cube[i][j][k].faces[2] ==  1)
                        instance.materials.get(0).set(ColorAttribute.createDiffuse(Color.BLUE));//colorize

                    instances.add(instance);

                    modelBuilder = new ModelBuilder();
                    model = modelBuilder.createBox(0.01f, .8f, .8f,
                            new Material(ColorAttribute.createDiffuse(Color.DARK_GRAY)),
                            Usage.Position | Usage.Normal);
                    instance = new GameObject(model, lexCube.cube[i][j][k], 3);//back-right face;
                    //rand = new Random();
                    if(lexCube.cube[i][j][k].faces[3] ==  1)
                        instance.materials.get(0).set(ColorAttribute.createDiffuse(Color.GREEN));//colorize

                    instances.add(instance);

                    modelBuilder = new ModelBuilder();
                    model = modelBuilder.createBox(0.01f, .8f, .8f,
                            new Material(ColorAttribute.createDiffuse(Color.DARK_GRAY)),
                            Usage.Position | Usage.Normal);
                    instance = new GameObject(model, lexCube.cube[i][j][k], 4);//front-right face;
                    //rand = new Random();
                    if(lexCube.cube[i][j][k].faces[4] ==  1)
                        instance.materials.get(0).set(ColorAttribute.createDiffuse(Color.RED));//colorize

                    instances.add(instance);

                    modelBuilder = new ModelBuilder();
                    model = modelBuilder.createBox(.8f, .8f, .01f,
                            new Material(ColorAttribute.createDiffuse(Color.DARK_GRAY)),
                            Usage.Position | Usage.Normal);
                    instance = new GameObject(model, lexCube.cube[i][j][k], 5);//back-right face;
                    //rand = new Random();
                    if(lexCube.cube[i][j][k].faces[5] ==  1)
                        instance.materials.get(0).set(ColorAttribute.createDiffuse(Color.MAGENTA));//colorize

                    instances.add(instance);

                }
            }
        }
        space = new ModelInstance(assets.get("spacesphere.obj", Model.class));
        loading = false;
    }
    @Override
    public void render() {
        if (loading && assets.update())
            doneLoading();
        //todo create 3 different states for screens
        camController.update();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        //offsets for camera
        float xOff = 0f;
        float yOff = 0f;
        float zOff = 0f;
        //level summary
        xOff = (cubeSize * -1f)/2f;
        yOff = (cubeSize * -1f)/2f;
        zOff = (cubeSize * -1f)/2f;

        //xOff = -1f;
        //yOff = -1f;
        //zOff = -1f;

        modelBatch.begin(cam);
        //modelBatch.render(instances, environment);
        if(screen ==1) {
            visibleCount = 0;
            for (final GameObject instance : instances) {
                if (isVisible(cam, instance)) {
                    float x = instance.blockref.getxPosition();
                    float y = instance.blockref.getyPosition();
                    float z = instance.blockref.getzPosition();

                    float xFace = 0f;
                    float yFace = 0f;
                    float zFace = 0f;

                    switch (instance.face) {
                        case -1: //cube
                            xFace = 0f;
                            yFace = 0f;
                            zFace = 0f;
                            break;
                        case 0: //0 : up
                            xFace = 0f;
                            yFace = 0.5f;
                            zFace = 0f;
                            break;
                        case 1: //1 : bottom
                            xFace = 0f;
                            yFace = -0.5f;
                            zFace = 0f;
                            break;
                        case 2: //2 : left-front
                            xFace = 0f;
                            yFace = 0f;
                            zFace = 0.5f;
                            break;
                        case 3: //3 : right-front
                            xFace = -0.5f;
                            yFace = 0f;
                            zFace = 0f;
                            break;
                        case 4: //4 : left-back
                            xFace = 0.5f;
                            yFace = 0f;
                            zFace = 0f;
                            break;
                        case 5: //5 : right-back
                            xFace = 0f;
                            yFace = 0f;
                            zFace = -0.5f;
                            break;
                    }

                    instance.transform.setToTranslation(x + xFace + xOff, y + yFace + yOff, z + zFace+ zOff);
                    //instance.calculateTransforms();//new
                    //instance.materials.get(0).set(ColorAttribute.createDiffuse(1, 1, 1, 1));//colorize
                    modelBatch.render(instance, environment);
                    visibleCount++;
                }
            }
        }
        // modelBatch.render(instance2, environment);
        if (space != null)
            modelBatch.render(space);
        modelBatch.end();


        stringBuilder.setLength(0);
        parLabel.setText(stringBuilder);//this is a hack
        stringBuilder.append(" FPS: ").append(Gdx.graphics.getFramesPerSecond());
        stringBuilder.append(" Visible: ").append(visibleCount);
        //stringBuilder.append(" Selected: ").append(selected);
        if (selectedCube != null)
            stringBuilder.append(" Selected: ").append(selectedCube.blockref.getxCoord() + "," + selectedCube.blockref.getyCoord() + "," + selectedCube.blockref.getzCoord());
        stringBuilder.append(" Debug: ").append(debugText);
        stringBuilder.append(" Solved: ").append(solvedText);
        stringBuilder.append(" Level: " + level + " Cube Size: " + cubeSize + " Par : " + turnCount);
        stringBuilder.append(" Moves: " + moves);
        label.setText(stringBuilder);
        //label.setPosition(100f, 100f);

        //stringBuilder.setLength(0);
        //levelCompleteLabel.;
        //levelCompleteLabel.setText(stringBuilder);
        if(screen ==0) {//300x200
            undoButton.setDraw(-500, -500);
            backButton.setDraw(-500, -500);

            titleImage.setDraw(Gdx.graphics.getWidth()/2 - 300/2, (Gdx.graphics.getHeight()/4)*4 - 200);
            newGameImage.setDraw(Gdx.graphics.getWidth()/2 - 300/2, (Gdx.graphics.getHeight()/4)*3 - 200);
            continueImage.setDraw(Gdx.graphics.getWidth()/2 - 300/2, (Gdx.graphics.getHeight()/4)*2 - 200);
            quitImage.setDraw(Gdx.graphics.getWidth()/2 - 300/2, (Gdx.graphics.getHeight()/4)*1 - 200);
        }
        else if(screen ==1) {
            titleImage.setDraw(-500, -500);
            newGameImage.setDraw(-500, -500);
            continueImage.setDraw(-500, -500);
            quitImage.setDraw(-500, -500);

            undoButton.setDraw(Gdx.graphics.getWidth() - 128, Gdx.graphics.getHeight() - 128);
            backButton.setDraw(0, Gdx.graphics.getHeight() - 128);

            stringBuilder.setLength(0);
            stringBuilder.append(turnCount+":"+moves);
            movesLabel.setFontScale(8f);
            movesLabel.setText(stringBuilder);
            movesLabel.setAlignment(Align.center);
            movesLabel.setSize(200,200);
            movesLabel.setPosition(Gdx.graphics.getWidth() - 200, 0);
        }

        if(levelComplete) {
            levelCompleteImage.setDraw(Gdx.graphics.getWidth()/2 - 384/2, (Gdx.graphics.getHeight()/4)*3 - 150/2);
            starImage.setDraw(Gdx.graphics.getWidth()/2 - 256/2, Gdx.graphics.getHeight()/4 -  256/2);

            stringBuilder.setLength(0);
            if(moves <= turnCount)
                stringBuilder.append("PAR");
            else {
                int x = moves - turnCount;
                stringBuilder.append(turnCount +" Over");
            }
            parLabel.setFontScale(3f);
            parLabel.setText(stringBuilder);
            parLabel.setAlignment(Align.center);
            parLabel.setSize(256,256);
            parLabel.setPosition(Gdx.graphics.getWidth()/2 - 256/2, Gdx.graphics.getHeight()/4 -  256/2);

        }
        else
        {
            levelCompleteImage.setDraw(-500, -500);
            starImage.setDraw(-500, -500);
        }

        stage.draw();

    }
    private Vector3 position = new Vector3();
    protected boolean isVisible(final Camera cam, final GameObject instance) {
        instance.transform.getTranslation(position);
        position.add(instance.center);
        return cam.frustum.sphereInFrustum(position, instance.radius);
        //return cam.frustum.boundsInFrustum(instance.bounds);
                //sphereInFrustum(position, instance.center);
    }

    @Override
    public void dispose() {
        saveState();
        modelBatch.dispose();
        instances.clear();
        assets.dispose();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }


    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        //todo create 3 states for screens
        if(screen == 0)
        {
            return false;
        }
        if(levelComplete)
        {
            debugText = "clicked on star";
            levelComplete = false;
            levelUpCube();
            return false;
        }
        if(screenX >= Gdx.graphics.getWidth() - 128 && screenY <= 128)
        {
            if(firstTurn) {

                return false;
            }
            lexCube.undo();
            firstTurn = true;
            moves--;

            debugText = "UNDO";
            return false;
        }
        else if(screenX <= 128 && screenY <= 128)
        {
            debugText = "BACK";
            //level = 1;
            //turnCount = 2;
            screen = 0;
            //cubeSize = 2;
            return false;
        }
        else {
            selecting = getObject(screenX, screenY);
            return selecting >= 0; //return true if you touched something, false else
        }
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(screen == 0)
        {
            return false;
        }
        return selecting >= 0;
    }//return true if you touched something, false else

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(screen == 0)
        {
            if(screenY < (Gdx.graphics.getHeight()/4)*1)
            {
                //debugText = "<1>";
            }
            else if(screenY < (Gdx.graphics.getHeight()/4)*2)
            {
                //New Game
                screen = 1;
                level = 1;
                resetCube();
                debugText = "<2>";
            }
            else if(screenY < (Gdx.graphics.getHeight()/4)*3)
            {
                //Continue
                screen = 1;
                resetCube();
                debugText = "<3>";
            }
            else if(screenY < (Gdx.graphics.getHeight()/4)*4)
            {
                //Quit Game
                debugText = "<4>";
                Gdx.app.exit();
            }
            return false;
        }
        if (selecting >= 0) {
            if (selecting == getObject(screenX, screenY)) {
                //setSelected(selecting);
                pushRow();

            }
            selecting = -1;
            return true;
        }
        return false;
    }
    public void pushRow()
    {
        firstTurn = false;
        moves++;
        //debugText = "IN!!!";
        //lexCube.cube[selectedCube.idX][selectedCube.idY][selectedCube.idZ].push(1,0,0);
        System.out.println("selected: "+ selectedCube.blockref.getxCoord() +":"+ selectedCube.blockref.getyCoord() +":" +selectedCube.blockref.getzCoord());

        int xFace = 0;
        int yFace = 0;
        int zFace = 0;

        switch (selectedCube.face)
        {
            case 0: //0 : up
                xFace = 0;
                yFace = -1;
                zFace = 0;
                break;
            case 1: //1 : bottom
                xFace = 0;
                yFace = 1;
                zFace = 0;
                break;
            case 2: //2 : left-front
                xFace = 0;
                yFace = 0;
                zFace = -1;
                break;
            case 3: //3 : right-front
                xFace = 1;
                yFace = 0;
                zFace = 0;
                break;
            case 4: //4 : left-back
                xFace = -1;
                yFace = 0;
                zFace = 0;
                break;
            case 5: //5 : right-back
                xFace = 0;
                yFace = 0;
                zFace = 1;
                break;
        }

        lexCube.deform(selectedCube.blockref.getxCoord(), selectedCube.blockref.getyCoord(), selectedCube.blockref.getzCoord(), xFace, yFace, zFace);
        //check for formed cube
        if(lexCube.isProper()) {
            solvedText = "SOLVED";
            completedLevel();
        }
        else
            solvedText = "" +
                    "!SOLVED";
    }
    public void completedLevel()
    {
        levelComplete = true;
        //resetCube();
    }
    GameObject intanceToPush;
    public int getObject (int screenX, int screenY) {
        Ray ray = cam.getPickRay(screenX, screenY);//obtain the pick ray from the camera

        int result = -1; //current object closer to the camera
        float distance = -1;//distance of the object to the camera

        BoundingBox bb = new BoundingBox();
        for (int i = 0; i < instances.size; ++i) {
            final GameObject instance = instances.get(i);//get instance i

            instance.transform.getTranslation(position);//get offset it to the center
            position.add(instance.center);

            if(instance.face == -1)//ignore cubes
                continue;
            /* This code uses projection from the center of the object to the ray to check which object is closest to the ray
            //project a vector from the center of the object to the closest point in the ray
            final float len = ray.direction.dot(position.x-ray.origin.x, position.y-ray.origin.y, position.z-ray.origin.z);
            if (len < 0f)//check if the object is behind the camera
                continue;
            //calculate distance between the center of the object to the closest point in the ray
            float dist2 = position.dst2(ray.origin.x+ray.direction.x*len, ray.origin.y+ray.direction.y*len, ray.origin.z+ray.direction.z*len);
            if (distance >= 0f && dist2 > distance)//object is further away from the screen than the precious one
                continue;

            //if (dist2 <= instance.radius * instance.radius) {
            if (dist2 <= instance.radius * instance.radius) {
                result = i;
                distance = dist2;
                selectedCube = instance;
            }
            */

            //this code uses an intersector

            //get correct bounding box
            bb = instance.calculateBoundingBox(bb).mul(instance.transform);//
            //System.out.println(bb);
            //get intersection
            Vector3 intersectionPoint = new Vector3();
            boolean intersects = Intersector.intersectRayBounds(ray, bb, intersectionPoint);
            if(intersects) {//ther eis an intersection
                System.out.println("intersects @ " + intersectionPoint.toString());

                float dist2 = ray.origin.dst2(intersectionPoint);
                if(dist2 < 0)//check if the point is behind the camera
                continue;
                if (distance < 0f)//this is the first collision
                {
                    result = i;
                    distance = dist2;
                    selectedCube = instance;
                }
                else if(dist2 < distance)//this is not the first one, but it;s closer than the previous one
                {
                    result = i;
                    distance = dist2;
                    selectedCube = instance;
                }
            }
            //else
            //    System.out.println("nothing @ " + intersectionPoint.toString());

            /*
            float dist2 = ray.origin.dst2(position);
            if (distance >= 0f && dist2 > distance)
                continue;
            //we can also use the intersector to get the position vector where the intersection happens
            //calculate distance squared and compare that :D
            //Intersector.intersectRaySphere(ray, position, instance.radius, null)
            System.out.println(instance.getTranslatedBoundingBox());
            if (Intersector.intersectRayBoundsFast(ray, instance.getTranslatedBoundingBox())){
                System.out.println("intersected: "+ instance.blockref.toString());
                result = i;
                distance = dist2;
                selectedCube = instance;
            }
            */
        }
        return result;
    }
    public void setSelected (int value) {
        if (selected == value) return;
        if (selected >= 0) {
            Material mat = instances.get(selected).materials.get(0);
            mat.clear();
            mat.set(originalMaterial);
        }
        selected = value;
        if (selected >= 0) {
            Material mat = instances.get(selected).materials.get(0);
            originalMaterial.clear();
            originalMaterial.set(mat);
            mat.clear();
            mat.set(selectionMaterial);
        }
    }
}