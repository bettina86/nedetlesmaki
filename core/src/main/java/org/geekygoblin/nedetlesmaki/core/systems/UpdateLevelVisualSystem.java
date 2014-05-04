/*
 * Copyright © 2013, Pierre Marijon <pierre@marijon.fr>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
 * copies of the Software, and to permit persons to whom the Software is 
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 *
 * The Software is provided "as is", without warranty of any kind, express or 
 * implied, including but not limited to the warranties of merchantability, 
 * fitness for a particular purpose and noninfringement. In no event shall the 
 * authors or copyright holders X be liable for any claim, damages or other 
 * liability, whether in an action of contract, tort or otherwise, arising from,
 * out of or in connection with the software or the use or other dealings in the
 * Software.
 */
package org.geekygoblin.nedetlesmaki.core.systems;

import javax.inject.Inject;
import javax.inject.Provider;

import java.util.ArrayList;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.VoidEntitySystem;
import javax.inject.Singleton;

import im.bci.jnuit.animation.IAnimationCollection;
import im.bci.jnuit.animation.PlayMode;

import org.geekygoblin.nedetlesmaki.core.manager.EntityIndexManager;
import im.bci.jnuit.artemis.sprite.Sprite;
import im.bci.jnuit.artemis.sprite.SpritePuppetControls;
import org.geekygoblin.nedetlesmaki.core.components.gamesystems.Position;
import org.geekygoblin.nedetlesmaki.core.constants.AnimationType;
import org.geekygoblin.nedetlesmaki.core.IAssets;
import org.geekygoblin.nedetlesmaki.core.components.Triggerable;
import org.geekygoblin.nedetlesmaki.core.utils.Mouvement;
import org.geekygoblin.nedetlesmaki.core.events.ShowLevelMenuTrigger;

import pythagoras.f.Vector3;

/**
 *
 * @author natir
 */
@Singleton
public class UpdateLevelVisualSystem extends VoidEntitySystem {

    private ComponentMapper<SpritePuppetControls> controlsMapper;

    private final IAssets assets;
    private int nbIndexSaved;
    private final EntityIndexManager index;
    private final GameSystem gameSystem;
    private final Provider<ShowLevelMenuTrigger> showLevelMenuTrigger;
    private IAnimationCollection nedAnim;
    private IAnimationCollection makiAnim;
    private IAnimationCollection boxAnim;
    private IAnimationCollection stairsAnim;
    private IAnimationCollection makiAnimBoost;

    @Inject
    public UpdateLevelVisualSystem(IAssets assets, EntityIndexManager indexSystem, GameSystem gameSystem, Provider<ShowLevelMenuTrigger> showLevelMenuTrigger) {
        this.assets = assets;
        this.nbIndexSaved = 0;
        this.index = indexSystem;
        this.gameSystem = gameSystem;
        this.showLevelMenuTrigger = showLevelMenuTrigger;
    }

    @Override
    protected void initialize() {
        controlsMapper = world.getMapper(SpritePuppetControls.class);
        nedAnim = this.assets.getAnimations("ned.json");
        makiAnim = this.assets.getAnimations("maki.json");
        boxAnim = this.assets.getAnimations("box.json");
        stairsAnim = this.assets.getAnimations("stairs.json");
        makiAnimBoost = this.assets.getAnimations("blue_maki_boost.json");
    }

    @Override
    protected void processSystem() {
        if (this.gameSystem.end) {
            if (gameSystem.endOfLevel()) {
                world.addEntity(world.createEntity().addComponent(new Triggerable(showLevelMenuTrigger.get())));
                this.gameSystem.end = false;
            }
        }

        ArrayList<Mouvement> rm = index.getRemove();
        if (rm != null) {
            for (int i = 0; i != rm.size(); i++) {
                for (int j = 0; j != rm.get(i).size(); j++) {
                    this.moveSprite(rm.get(i).getEntity(), rm.get(i).getPosition(j), rm.get(i).getAnimation(j), rm.get(i).getBeforeWait(j), rm.get(i).getAnimationTime(j));
                }
            }

            this.index.setRemove(null);
        }

        if (index.sizeOfStack() > nbIndexSaved) {
            ArrayList<Mouvement> change = this.index.getChangement();

            if (change != null) {
                for (int i = 0; i != change.size(); i++) {
                    for (int j = 0; j != change.get(i).size(); j++) {
                        this.moveSprite(change.get(i).getEntity(), change.get(i).getPosition(j), change.get(i).getAnimation(j), change.get(i).getBeforeWait(j), change.get(i).getAnimationTime(j));
                    }
                }
            }
        }
        nbIndexSaved = index.sizeOfStack();
    }

    private void moveSprite(Entity e, Position diff, AnimationType a, float waitBefore, float animationTime) {

        Sprite sprite = e.getComponent(Sprite.class);
        SpritePuppetControls updatable = this.controlsMapper.getSafe(e);
        if (updatable == null) {
            updatable = new SpritePuppetControls(sprite);
        }

        if (a == AnimationType.no) {
            updatable.waitDuring(waitBefore)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime);
        } else if (a == AnimationType.ned_right) {
            updatable.startAnimation(nedAnim.getAnimationByName("walk_right"))
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime)
                    .stopAnimation();
        } else if (a == AnimationType.ned_left) {
            updatable.startAnimation(nedAnim.getAnimationByName("walk_left"))
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime)
                    .stopAnimation();
        } else if (a == AnimationType.ned_down) {
            updatable.startAnimation(nedAnim.getAnimationByName("walk_down"))
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime)
                    .stopAnimation();
        } else if (a == AnimationType.ned_up) {
            updatable.startAnimation(nedAnim.getAnimationByName("walk_up"))
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime)
                    .stopAnimation();
        } else if (a == AnimationType.ned_push_right) {
            updatable.startAnimation(nedAnim.getAnimationByName("push_right"))
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime)
                    .stopAnimation();
        } else if (a == AnimationType.ned_push_left) {
            updatable.startAnimation(nedAnim.getAnimationByName("push_left"))
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime)
                    .stopAnimation();
        } else if (a == AnimationType.ned_push_down) {
            updatable.startAnimation(nedAnim.getAnimationByName("push_down"))
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime)
                    .stopAnimation();
        } else if (a == AnimationType.ned_push_up) {
            updatable.startAnimation(nedAnim.getAnimationByName("push_up"))
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime)
                    .stopAnimation();
        } else if (a == AnimationType.ned_waits_boost_up) {
            updatable.startAnimation(nedAnim.getAnimationByName("ned_waits_boost_start_up"), PlayMode.ONCE)
                    .waitAnimation()
                    .startAnimation(nedAnim.getAnimationByName("ned_waits_boost_stop_up"), PlayMode.ONCE);
        } else if (a == AnimationType.ned_waits_boost_down) {
            updatable.startAnimation(nedAnim.getAnimationByName("ned_waits_boost_start_down"), PlayMode.ONCE)
                    .waitAnimation()
                    .startAnimation(nedAnim.getAnimationByName("ned_waits_boost_stop_down"), PlayMode.ONCE);
        } else if (a == AnimationType.ned_waits_boost_right) {
            updatable.startAnimation(nedAnim.getAnimationByName("ned_waits_boost_start_right"), PlayMode.ONCE)
                    .waitAnimation()
                    .startAnimation(nedAnim.getAnimationByName("ned_waits_boost_stop_right"), PlayMode.ONCE);
        } else if (a == AnimationType.ned_waits_boost_left) {
            updatable.startAnimation(nedAnim.getAnimationByName("ned_waits_boost_start_left"), PlayMode.ONCE)
                    .waitAnimation()
                    .startAnimation(nedAnim.getAnimationByName("ned_waits_boost_stop_left"), PlayMode.ONCE);
        } else if (a == AnimationType.ned_mount_stairs_up) {
            updatable.startAnimation(nedAnim.getAnimationByName("ned_mount_up"))
                    .moveToRelative(new Vector3(diff.getY() - 0.4f, diff.getX() - 0.2f, 1), animationTime)
                    .stopAnimation();
        } else if (a == AnimationType.ned_mount_stairs_down) {
            updatable.startAnimation(nedAnim.getAnimationByName("ned_mount_down"))
                    .moveToRelative(new Vector3(diff.getY() + 0.2f, diff.getX() - 0.4f, 1), animationTime)
                    .stopAnimation();
        } else if (a == AnimationType.ned_mount_stairs_right) {
            updatable.startAnimation(nedAnim.getAnimationByName("ned_mount_right"))
                    .moveToRelative(new Vector3(diff.getY() - 0.2f, diff.getX() + 0.2f, 1), animationTime)
                    .stopAnimation();
        } else if (a == AnimationType.ned_mount_stairs_left) {
            updatable.startAnimation(nedAnim.getAnimationByName("ned_mount_left"))
                    .moveToRelative(new Vector3(diff.getY() - 0.2f, diff.getX() - 0.4f, 1), animationTime)
                    .stopAnimation();
        } else if (a == AnimationType.box_destroy) {
            updatable.waitDuring(waitBefore)
                    .startAnimation(boxAnim.getAnimationByName("destroy"), PlayMode.ONCE);
            this.index.disabled(e);
        } else if (a == AnimationType.box_boom) {
            updatable.waitDuring(waitBefore)
                    .startAnimation(boxAnim.getAnimationByName("box_boom"), PlayMode.ONCE);
            this.index.disabled(e);
        } else if (a == AnimationType.box_create) {
            this.index.enabled(e);
            updatable.startAnimation(boxAnim.getAnimationByName("create"), PlayMode.ONCE);
        } else if (a == AnimationType.maki_green_one) {
            updatable.waitDuring(waitBefore)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime)
                    .startAnimation(makiAnim.getAnimationByName("maki_green_one"), PlayMode.ONCE);
        } else if (a == AnimationType.maki_orange_one) {
            updatable.waitDuring(waitBefore)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime)
                    .startAnimation(makiAnim.getAnimationByName("maki_orange_one"), PlayMode.ONCE);
        } else if (a == AnimationType.maki_blue_one) {
            updatable.waitDuring(waitBefore)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime)
                    .startAnimation(makiAnim.getAnimationByName("maki_blue_one"), PlayMode.ONCE);
        } else if (a == AnimationType.maki_green_out) {
            updatable.startAnimation(makiAnim.getAnimationByName("maki_green_out"), PlayMode.ONCE)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime);
        } else if (a == AnimationType.maki_orange_out) {
            updatable.startAnimation(makiAnim.getAnimationByName("maki_orange_out"), PlayMode.ONCE)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime);
        } else if (a == AnimationType.maki_blue_out) {
            updatable.startAnimation(makiAnim.getAnimationByName("maki_blue_out"), PlayMode.ONCE)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime);
        } else if (a == AnimationType.maki_orange_no) {
            updatable.waitDuring(waitBefore)
                    .startAnimation(makiAnim.getAnimationByName("maki_orange_no"))
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime);
        } else if (a == AnimationType.disable_entity) {
            e.disable();
        } else if (a == AnimationType.stairs_open_up) {
            updatable.waitDuring(waitBefore)
                    .startAnimation(stairsAnim.getAnimationByName("stairs_up_open"), PlayMode.ONCE)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime)
                    .waitAnimation();
        } else if (a == AnimationType.stairs_open_down) {
            updatable.waitDuring(waitBefore)
                    .startAnimation(stairsAnim.getAnimationByName("stairs_down_open"), PlayMode.ONCE)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime)
                    .waitAnimation();
        } else if (a == AnimationType.stairs_open_left) {
            updatable.waitDuring(waitBefore)
                    .startAnimation(stairsAnim.getAnimationByName("stairs_left_open"), PlayMode.ONCE)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime)
                    .waitAnimation();
        } else if (a == AnimationType.stairs_open_right) {
            updatable.waitDuring(waitBefore)
                    .startAnimation(stairsAnim.getAnimationByName("stairs_right_open"), PlayMode.ONCE)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime)
                    .waitAnimation();
        } else if (a == AnimationType.stairs_close_up) {
            index.getStairs(e).setStairs(false);
            updatable.waitDuring(waitBefore)
                    .startAnimation(stairsAnim.getAnimationByName("stairs_up_close"), PlayMode.ONCE)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime)
                    .waitAnimation();
        } else if (a == AnimationType.stairs_close_down) {
            index.getStairs(e).setStairs(false);
            updatable.waitDuring(waitBefore)
                    .startAnimation(stairsAnim.getAnimationByName("stairs_down_close"), PlayMode.ONCE)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime)
                    .waitAnimation();
        } else if (a == AnimationType.stairs_close_left) {
            index.getStairs(e).setStairs(false);
            updatable.waitDuring(waitBefore)
                    .startAnimation(stairsAnim.getAnimationByName("stairs_left_close"), PlayMode.ONCE)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime)
                    .waitAnimation();
        } else if (a == AnimationType.stairs_close_right) {
            index.getStairs(e).setStairs(false);
            updatable.waitDuring(waitBefore)
                    .startAnimation(stairsAnim.getAnimationByName("stairs_right_close"), PlayMode.ONCE)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime)
                    .waitAnimation();
        } else if (a == AnimationType.boost_start_up) {
            updatable.waitDuring(waitBefore)
                    .startAnimation(makiAnimBoost.getAnimationByName("boost_start_up"), PlayMode.ONCE)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime);
        } else if (a == AnimationType.boost_start_down) {
            updatable.waitDuring(waitBefore)
                    .startAnimation(makiAnimBoost.getAnimationByName("boost_start_down"), PlayMode.ONCE)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime);
        } else if (a == AnimationType.boost_start_left) {
            updatable.waitDuring(waitBefore)
                    .startAnimation(makiAnimBoost.getAnimationByName("boost_start_left"), PlayMode.ONCE)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime);
        } else if (a == AnimationType.boost_start_right) {
            updatable.waitDuring(waitBefore)
                    .startAnimation(makiAnimBoost.getAnimationByName("boost_start_right"), PlayMode.ONCE)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime);
        } else if (a == AnimationType.boost_stop_up) {
            updatable.waitDuring(waitBefore)
                    .startAnimation(makiAnimBoost.getAnimationByName("boost_stop_up"), PlayMode.ONCE)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime)
                    .startAnimation(makiAnimBoost.getAnimationByName("boost_clean_up"), PlayMode.ONCE);
        } else if (a == AnimationType.boost_stop_down) {
            updatable.waitDuring(waitBefore)
                    .startAnimation(makiAnimBoost.getAnimationByName("boost_stop_down"), PlayMode.ONCE)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime)
                    .startAnimation(makiAnimBoost.getAnimationByName("boost_clean_down"), PlayMode.ONCE);
        } else if (a == AnimationType.boost_stop_left) {
            updatable.waitDuring(waitBefore)
                    .startAnimation(makiAnimBoost.getAnimationByName("boost_stop_left"), PlayMode.ONCE)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime)
                    .startAnimation(makiAnimBoost.getAnimationByName("boost_clean_left"), PlayMode.ONCE);
        } else if (a == AnimationType.boost_stop_right) {
            updatable.waitDuring(waitBefore)
                    .startAnimation(makiAnimBoost.getAnimationByName("boost_stop_right"), PlayMode.ONCE)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime)
                    .startAnimation(makiAnimBoost.getAnimationByName("boost_clean_right"), PlayMode.ONCE);
        } else if (a == AnimationType.boost_loop_up) {
            updatable.startAnimation(makiAnimBoost.getAnimationByName("boost_loop_up"), PlayMode.LOOP)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime);
        } else if (a == AnimationType.boost_loop_down) {
            updatable.startAnimation(makiAnimBoost.getAnimationByName("boost_loop_down"), PlayMode.LOOP)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime);
        } else if (a == AnimationType.boost_loop_left) {
            updatable.startAnimation(makiAnimBoost.getAnimationByName("boost_loop_left"), PlayMode.LOOP)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime);
        } else if (a == AnimationType.boost_loop_right) {
            updatable.startAnimation(makiAnimBoost.getAnimationByName("boost_loop_right"), PlayMode.LOOP)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime);
        } else if (a == AnimationType.fly_start_up) {
            updatable.waitDuring(waitBefore)
                    .startAnimation(nedAnim.getAnimationByName("fly_start_up"), PlayMode.ONCE)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime);
        } else if (a == AnimationType.fly_start_down) {
            updatable.waitDuring(waitBefore)
                    .startAnimation(nedAnim.getAnimationByName("fly_start_down"), PlayMode.ONCE)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime);
        } else if (a == AnimationType.fly_start_left) {
            updatable.waitDuring(waitBefore)
                    .startAnimation(nedAnim.getAnimationByName("fly_start_left"), PlayMode.ONCE)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime);
        } else if (a == AnimationType.fly_start_right) {
            updatable.waitDuring(waitBefore)
                    .startAnimation(nedAnim.getAnimationByName("fly_start_right"), PlayMode.ONCE)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime);
        } else if (a == AnimationType.fly_stop_up) {
            updatable.waitDuring(waitBefore)
                    .startAnimation(nedAnim.getAnimationByName("fly_stop_up"), PlayMode.ONCE)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime)
                    .waitAnimation();
        } else if (a == AnimationType.fly_stop_down) {
            updatable.waitDuring(waitBefore)
                    .startAnimation(nedAnim.getAnimationByName("fly_stop_down"), PlayMode.ONCE)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime)
                    .waitAnimation();
        } else if (a == AnimationType.fly_stop_left) {
            updatable.waitDuring(waitBefore)
                    .startAnimation(nedAnim.getAnimationByName("fly_stop_left"), PlayMode.ONCE)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime)
                    .waitAnimation();
        } else if (a == AnimationType.fly_stop_right) {
            updatable.waitDuring(waitBefore)
                    .startAnimation(nedAnim.getAnimationByName("fly_stop_right"), PlayMode.ONCE)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime)
                    .waitAnimation();
        } else if (a == AnimationType.fly_loop_up) {
            updatable.waitDuring(waitBefore)
                    .startAnimation(nedAnim.getAnimationByName("fly_loop_up"), PlayMode.LOOP)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime);
        } else if (a == AnimationType.fly_loop_down) {
            updatable.waitDuring(waitBefore)
                    .startAnimation(nedAnim.getAnimationByName("fly_loop_down"), PlayMode.LOOP)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime);
        } else if (a == AnimationType.fly_loop_left) {
            updatable.waitDuring(waitBefore)
                    .startAnimation(nedAnim.getAnimationByName("fly_loop_left"), PlayMode.LOOP)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime);
        } else if (a == AnimationType.fly_loop_right) {
            updatable.waitDuring(waitBefore)
                    .startAnimation(nedAnim.getAnimationByName("fly_loop_right"), PlayMode.LOOP)
                    .moveToRelative(new Vector3(diff.getY(), diff.getX(), 0), animationTime);
        }

        e.addComponent(updatable);
        e.changedInWorld();
    }
}
