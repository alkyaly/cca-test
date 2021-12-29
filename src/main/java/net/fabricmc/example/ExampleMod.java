package net.fabricmc.example;

import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.ComponentFactory;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExampleMod implements ChunkComponentInitializer, ModInitializer {

	public static final ComponentKey<ComponentTest> TEST = ComponentRegistry
			.getOrCreate(new Identifier("test", "test"), ComponentTest.class);

	@Override
	public void onInitialize() {
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			var player = handler.player;
			var pos = new ChunkPos(((ServerWorld) player.world).getSpawnPos());

			var cmp = TEST.getNullable(player.world.getChunk(pos.x, pos.z));
			System.out.println(pos);

			if (cmp != null) {
				System.out.println(cmp.getThing()); // prints 0 on 1st join and then in rejoin
				cmp.setThing(cmp.getThing() + 1337);
				System.out.println(cmp.getThing()); // 1337 on both too
			}
		});
	}

	@Override
	public void registerChunkComponentFactories(ChunkComponentFactoryRegistry registry) {
		registry.register(TEST, c -> {
			if (c instanceof WorldChunk ch) {
				return new ComponentTest.ComponentTestImpl(ch);
			}
			return new ComponentTest() {
				@Override
				public void setThing(int set) {
				}

				@Override
				public int getThing() {
					return -1;
				}

				@Override
				public void readFromNbt(NbtCompound tag) {
				}

				@Override
				public void writeToNbt(NbtCompound tag) {
				}
			};
		});
	}
}
