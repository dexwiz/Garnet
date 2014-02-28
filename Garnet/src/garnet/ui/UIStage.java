package garnet.ui;

import garnet.board.ControllerSystem;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.event.ChangeEvent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Pool;

public class UIStage extends Stage
{
	private Skin skin;

	//banner table
	
	//status table
	//contains object and team table



	private Table uiTable;
	private Table selectedTable;
	private Table selectedBuffTable;
	private Table selectedStatsTable;
	private Table middleTable;
	private Table targetedTable;
	private Table targetedBuffTable;
	private Table targetedStatsTable;
	private Table abilityBarTable;
	
	//object table widgets
	private Label selectedName;
	private Label selectedHP;
	private Label selectedMana;
	private ArrayList<BuffIcon> selectedBuffIcons = new ArrayList<BuffIcon>();
	
	private TextButton execute;
	
	private Label targetedName;
	private Label targetedHP;
	private Label targetedMana;
	private ArrayList<BuffIcon> targetedBuffIcons = new ArrayList<BuffIcon>();
	private Label toolTip;
	
	private int buffsPerRow = 4;
	private int maxBuffs = 16;

	private Pool<BuffIcon> buffIconPool = new Pool<BuffIcon>() {
		
		protected BuffIcon newObject()
		{
			final BuffIcon icon =  new BuffIcon();
			Gdx.app.log("Buff Icon Pool", "Creating new buff icon");
			icon.addListener(new ClickListener()
			{
				@Override
				public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor)
				{
					setToolTip(icon.getToolTip());
				}
				
				@Override
				public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor)
				{
					clearToolTip();
				}
			});
			
			
			return icon;
		}
	};

	private ButtonGroup group;
	private int maxButtons = 5;
	private ArrayList<ImageButton> abilityButtons;

	private TextButton endTurn;
	
	private SpriteBatch batch;

	private ControllerSystem currentController;	
	
	
	public UIStage(float width, float height, boolean keepAspectRatio)
	{		
		super(width, height, keepAspectRatio);
//		skin = new Skin(Gdx.files.internal("ui2/ui.json"));
		batch = new SpriteBatch();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/visitor1.ttf"));
        BitmapFont font9 = generator.generateFont(9);
        BitmapFont font10 = generator.generateFont(10);
        BitmapFont font14 = generator.generateFont(14);
        BitmapFont font18 = generator.generateFont(18);
        BitmapFont font20 = generator.generateFont(20);


        generator.dispose();
		
		 // A skin can be loaded via JSON or defined programmatically, either is fine. Using a skin is optional but strongly
        // recommended solely for the convenience of getting a texture, region, etc as a drawable, tinted drawable, etc.
        skin = new Skin();


        // Generate a 1x1 white texture and store it in the skin named "white".
        Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));
        
        

        Pixmap pixmap2 = new Pixmap(1, 1, Format.RGBA8888);
        pixmap2.setColor(Color.BLUE);
        pixmap2.fill();
        pixmap2.setColor(1, 1, 1, 1);

        skin.add("transparent", new Texture(pixmap2));
        
        // Store the default libgdx font under the name "default".
        skin.add("default", font20);

        // Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.disabled = skin.newDrawable("white", Color.RED);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);
        skin.add("toggle", textButtonStyle);


        LabelStyle labelStyle = new LabelStyle();
//        labelStyle.background = skin.newDrawable("white", Color.DARK_GRAY);
        labelStyle.font = skin.getFont("default");
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

        //uiTable holds all UI parts
        uiTable = new Table();

        selectedTable = new Table();
        middleTable = new Table();
        targetedTable = new Table();
        
//        selectedTable
//        middleTable
//        targetedTable
        
        this.addActor(uiTable);

        
        uiTable.debug();
//        selectedTable.debug();
        middleTable.debug();
//        targetedTable.debug();

        uiTable.bottom();

//        selectedTable.bottom();
//        middleTable.bottom();
//        targetedTable.bottom();
      
        uiTable.setFillParent(true);
        
//        uiTable.setHeight(500);
        
//        uiTable.setPosition(Gdx.graphics.getWidth()/2, 100f);

        uiTable.add(selectedTable).minWidth(150);
        uiTable.add(middleTable).bottom();
        uiTable.add(targetedTable).minWidth(150);
        
        //fills out the selected column of the UI table
        selectedBuffTable = new Table();
        selectedStatsTable = new Table();
        selectedTable.add(selectedBuffTable).expandX();
        selectedTable.row();
        selectedTable.add(selectedStatsTable);
        
		selectedName = new Label("", skin);
		selectedHP = new Label("HP", skin);
		selectedMana = new Label("Mana", skin);
		selectedStatsTable.add(selectedName).row().expandX();
		selectedStatsTable.add(selectedHP).row();
		selectedStatsTable.add(selectedMana).row();
		
		
		//fills out the targeted column of the UI table
        targetedBuffTable = new Table();
        targetedStatsTable = new Table();
        targetedTable.add(targetedBuffTable).expandX();
        targetedTable.row();
        targetedTable.add(targetedStatsTable);
        
		targetedName = new Label("", skin);
		targetedHP = new Label("HP", skin);
        targetedMana = new Label("Mana", skin);
        targetedStatsTable.add(targetedName).row().expandX();
        targetedStatsTable.add(targetedHP).row();
        targetedStatsTable.add(targetedMana).row();
        
        //fills out the middle column of the UI table
		toolTip = new Label("TOOLTIP", skin);  
        abilityBarTable = new Table();
		execute = new TextButton("Execute", skin);
		execute.addListener(new ClickListener()
		{

			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				executeButtonClick();
			}
			
		});
		middleTable.add(toolTip).row().expandX();
		middleTable.add(abilityBarTable).row();
		middleTable.add(execute).row();
      
		
        endTurn = new TextButton("End Turn", skin);
        endTurn.addListener(new ChangeListener()
        {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				event.cancel();
			}
        	
        });
        endTurn.addListener(new ClickListener()
		{

			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				endTurnButtonClick();
			}
			
		});
		this.addActor(endTurn);
        
        

	
		//Creates ability buttons
		group = new ButtonGroup();
		group.setMinCheckCount(0);
		group.setMaxCheckCount(1);
		abilityButtons = new ArrayList<ImageButton>();
		for(int i = 0; i < maxButtons; i++)
			{
				Texture abilityTexture = new Texture(Gdx.files.internal("sprites/abilities/ability.png"));
				abilityTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
				TextureRegion region = new TextureRegion(abilityTexture, 16, 16, 32, 32);
				TextureRegionDrawable regionDrawable = new TextureRegionDrawable(region);
				
				Skin skin = new Skin();
				ImageButtonStyle imageStyle = new ImageButtonStyle();	
				imageStyle.imageUp = skin.newDrawable(regionDrawable, Color.WHITE);
				imageStyle.imageDown = skin.newDrawable(regionDrawable, Color.GRAY);
				imageStyle.imageOver = skin.newDrawable(regionDrawable, Color.LIGHT_GRAY);
				imageStyle.imageChecked = skin.newDrawable(regionDrawable, Color.GREEN);
//				imageStyle.imageCheckedOver = skin.newDrawable(regionDrawable, Color.LIGHT_GRAY);
				imageStyle.imageDisabled = skin.newDrawable(regionDrawable, Color.DARK_GRAY);
				
		        skin.add("default", imageStyle);
		        skin.add("toggle", imageStyle);
		        
				ImageButton button = new ImageButton(skin, "toggle");
				button.setColor(1,1,1, 1);
				final int index = i;
				button.addListener(new ClickListener()
				{
					@Override
					public void clicked(InputEvent event, float x, float y) {
						super.clicked(event, x, y);
						abilityButtonClick(index);
						Gdx.app.log("Button", "Yo, ability button clicked: " + index);
					}
				});
	
				
				button.pad(0);
				button.padLeft(5f);
				button.setName(Integer.toString(i));
				abilityButtons.add(button);
				group.add(button);
				abilityBarTable.add(button);
			}


//		endTurn.setFillParent(t

	}


	@Override
	public void act() {
		super.act();
		
		
		for(int i = 0; i < abilityButtons.size(); i++)
		{
//			abilityButtons.get(i).setVisible(false);
			abilityButtons.get(i).setDisabled(true);
			abilityButtons.get(i).setName("Button");
			if(abilityButtons.get(i).isChecked()) abilityButtons.get(i).toggle();
		}	
		
		for(BuffIcon icon : this.selectedBuffIcons)
		{
			buffIconPool.free(icon);
		}
		this.selectedBuffIcons.clear();
		
		HashMap<String, Object> info = this.currentController.getSelectedInfo();
		HashMap<String, String> targtedInfo;
		if(info != null)
		{
			//sets Selected Unit
			this.selectedName.setText((CharSequence) info.get("name"));
			this.selectedHP.setText((CharSequence) info.get("hp"));
			this.targetedMana.setText((CharSequence) info.get("mana"));
				
			//defines ability bar
			ArrayList<HashMap<String, String>> abilityInfo = this.currentController.getAbilityBarInfo();
			for(int i = 0; i < abilityInfo.size() && i < abilityButtons.size(); i++)
			{
				abilityButtons.get(i).setVisible(true);
				abilityButtons.get(i).setName(abilityInfo.get(i).get("name"));
				abilityButtons.get(i).setDisabled(false);
			}
			
			
			int buffcount = (Integer) info.get("buffcount");
//			Gdx.app.log("UI", "Displaying " + buffcount + " buffs");
//			Gdx.app.log("UI", "Pool peak: " + buffIconPool.peak + ", Pool free " + buffIconPool.getFree() );
			for(int i = 0; i < buffcount; i++)
			{
				BuffIcon icon = this.buffIconPool.obtain();
				icon.setVisible(true);
				icon.setToolTip((String) info.get("bufftooltip" + i));
				this.selectedBuffIcons.add(icon);
				this.selectedBuffTable.add(icon);
				
				if ((i+1) % this.buffsPerRow == 0) this.selectedBuffTable.row();


			}
			
			//if there is a selected unit then also looks for a targeted unit
			targtedInfo = this.currentController.getTargetedInfo();
			if(targtedInfo != null)
			{
				this.targetedName.setText(targtedInfo.get("name"));
				this.targetedHP.setText(targtedInfo.get("hp"));
//				this.mana.setText(info.get("mana")));
			}		
			else
			{
				clearTargeted();
			}
			this.execute.setDisabled(!this.currentController.isSelectedAbilityCastable());
		}		
		else
		{
			clearSelected();
		}
	}
	
	private void clearSelected()
	{
		this.selectedName.setText("");
		this.selectedHP.setText("");
		this.targetedMana.setText("");
		for(int i = 0; i < abilityButtons.size(); i++)
		{
			abilityButtons.get(i).setDisabled(true);
//			abilityButtons.get(i).setVisible(false);
		}
		clearTargeted();
	}
	
	private void clearTargeted()
	{
		this.targetedName.setText("");
		this.targetedHP.setText("");
	}
	


	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(!super.touchDown(screenX, screenY, pointer, button))
		{
			this.currentController.touchBoard(screenX, screenY);

		}
		return true;
	}


	@Override
	public void draw()
	{
			
			getCamera().update();
			if (!getRoot().isVisible()) return;
			batch.setProjectionMatrix(getCamera().combined);
			batch.begin();
			batch.enableBlending();
			getRoot().draw(batch, 1);
			Table.drawDebug(this);
			batch.end();
	}
	
	
	public void abilityButtonClick(int index)
	{
		this.currentController.selectAbility(index);
	}

	public void executeButtonClick()
	{
		this.currentController.castSelectedAbility();
	}
	
	public void endTurnButtonClick()
	{
		this.currentController.endTurn();
	}
		
	public void setSelectedBuffs()
	{
		
	}	/**
	 * Intializes buttons
	 * @param i Index of the button
	 * @param text Text of the button
	 * @param enabled If the button should be enabled or not
	 */
	public void setAbilityButton(int i, String text, TextureRegion region, boolean enabled)
	{
		if(i < abilityButtons.size())
		{
			abilityButtons.get(i).setName(text);
			abilityButtons.get(i).setDisabled(!enabled);
			abilityButtons.get(i).setVisible(true);
			if(region != null)
			{
				ImageButtonStyle imageStyle = abilityButtons.get(i).getStyle();

//				TextureRegionDrawable regionDrawable = new TextureRegionDrawable(region);
//				Sprite sprite = new Sprite(region);
//				SpriteDrawable sDrawable = new SpriteDrawable(sprite);
//				
//
//				imageStyle.imageUp = sDrawable;
//				imageStyle.imageDown = sDrawable;
//				imageStyle.imageOver = sDrawable;

				
				AbilityButtonDrawable drawable = new AbilityButtonDrawable(region);
				imageStyle.imageUp = drawable.newAbilityButtonDrawable(Color.WHITE);
				imageStyle.imageDown = drawable.newAbilityButtonDrawable(Color.WHITE);
				imageStyle.imageOver = drawable.newAbilityButtonDrawable(Color.RED);
				imageStyle.imageChecked = drawable.newAbilityButtonDrawable(Color.WHITE);
				imageStyle.imageCheckedOver = drawable.newAbilityButtonDrawable(Color.WHITE);
				imageStyle.imageDisabled = drawable.newAbilityButtonDrawable(Color.WHITE);
			}
		}	
	}
	
	public void resetActiveBuffIcons()
	{
		for(BuffIcon icon : selectedBuffIcons)
		{
			icon.reset();
		}
	}
	
	public void resetSelectedBuffIcons()
	{
		for(BuffIcon icon : targetedBuffIcons)
		{
			icon.reset();
		}
	}
	
	public void resetSelected()
	{
		this.selectedName.setText("NAME");
		this.selectedHP.setText("HP");
		
//		moveButton.setVisible(false);
//		moveButton.setDisabled(true);
		
		for(int i = 0; i < abilityButtons.size(); i++)
		{
//			abilityButtons.get(i).setVisible(false);
			abilityButtons.get(i).setDisabled(true);
			abilityButtons.get(i).setName("Button");
			if(abilityButtons.get(i).isChecked()) abilityButtons.get(i).toggle();
		}	
		
		resetActiveBuffIcons();
	}
	
	public void deselectButtons()
	{
		for(Button button : group.getButtons())
		{
			button.setChecked(false);
		}
	}



	
	public void setToolTip(CharSequence charSequence)
	{
		if(charSequence.length() == 0)
		{
			toolTip.setText(" ");
		}
		else
		{
			toolTip.setText(charSequence);

		}
//		toolTip.setHeight(50);

	}
	
	public void clearToolTip()
	{
		toolTip.setText(" ");
//		toolTip.
//		toolTip.setSize(100, 100);
	}
	


	public void setController(ControllerSystem controllerSystem) {
		this.currentController = controllerSystem;		
		if(this.currentController != null) Gdx.app.log("UI", "UI has been given a non null controller");
	}

}
