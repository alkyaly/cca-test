package net.fabricmc.example;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.chunk.WorldChunk;

public interface ComponentTest extends Component {

    void setThing(int set);
    int getThing();

    class ComponentTestImpl implements ComponentTest {

        private final WorldChunk chunk;
        private int thing;

        public ComponentTestImpl(WorldChunk chunk) {
            this.chunk = chunk;
        }

        @Override
        public void setThing(int set) {
            this.thing = set;
            markDirty();
        }

        @Override
        public int getThing() {
            return thing;
        }

        private void markDirty() {
            this.chunk.setShouldSave(true);
        }

        @Override
        public void readFromNbt(NbtCompound tag) {
            this.thing = tag.getInt("aaa");
        }

        @Override
        public void writeToNbt(NbtCompound tag) {
            tag.putInt("aaa", thing);
        }
    }
}
