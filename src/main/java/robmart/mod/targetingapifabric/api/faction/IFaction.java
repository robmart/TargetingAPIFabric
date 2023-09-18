package robmart.mod.targetingapifabric.api.faction;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;

import java.util.Map;
import java.util.Set;

/**
 * Created by Robmart.
 * <p>
 * This software is a modification for the game Minecraft.
 * Copyright (C) 2020 Robmart
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public interface IFaction {

    String getName();
    boolean setName(String name);
    boolean getIsPermanent();
    void addFriendClass(Class<? extends Entity> classToAdd);
    void addFriendEntity(Entity entityToAdd);
    void removeFriendClass(Class<? extends Entity> classToRemove);
    void removeFriendEntity(Entity entityToRemove);
    void addMemberClass(Class<? extends Entity> classToAdd);
    void addMemberEntity(Entity entityToAdd);
    void removeMemberClass(Class<? extends Entity> classToRemove);
    void removeMemberEntity(Entity entityToRemove);
    void addEnemyClass(Class<? extends Entity> classToAdd);
    void addEnemyEntity(Entity entityToAdd);
    void removeEnemyClass(Class<? extends Entity> classToRemove);
    void removeEnemyEntity(Entity entityToRemove);
    Map<Object, Boolean> getAllMembers();
    Map<Object, Boolean> getAllFriends();
    Map<Object, Boolean> getAllEnemies();
    void clearMembers();
    void clearFriends();
    void clearEnemies();
    boolean isMember(Class<? extends Entity> potentialMember);
    boolean isMember(Entity potentialMember);
    boolean isFriend(Class<? extends Entity> potentialFriend);
    boolean isFriend(Entity potentialFriend);
    boolean isEnemy(Class<? extends Entity> potentialEnemy);
    boolean isEnemy(Entity potentialEnemy);
    void onDisband();
    void sync();
    void readFromNbt(NbtCompound nbt);
    NbtCompound writeToNbt(NbtCompound nbt);
}
