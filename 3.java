import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class BinaryHelper {

    private ByteBuffer buffer;

    public BinaryHelper(int initialSize) {
        buffer = ByteBuffer.allocate(initialSize);
        buffer.order(ByteOrder.LITTLE_ENDIAN); // 设置为小端模式，根据实际需求调整
    }

    public byte readByte(int index) {
        checkIndex(index);
        return buffer.get(index);
    }

    public void writeByte(int index, byte value) {
        ensureCapacity(index + 1);
        buffer.put(index, value);
    }

    public void writeBytes(byte[] data) {
        ensureCapacity(buffer.position() + data.length);
        buffer.put(data);
    }

    // 用于位操作的辅助方法
    public static boolean getBit(byte b, int position) {
        return ((b >> position) & 1) == 1;
    }

    public boolean getBit(int index, int position) {
        checkIndex(index);
        byte b = buffer.get(index);
        return getBit(b, position);
    }

    public static byte setBit(byte b, int position, boolean value) {
        if (position < 0 || position > 7) {
            throw new IllegalArgumentException("Invalid bit position");
        }
        if (value) {
            // set bit
            return (byte) (b | (1 << position));
        } else {
            // unset bit
            return (byte) (b & ~(1 << position));
        }
    }

    public void setBit(int index, int position, boolean value) {
        checkIndex(index);
        byte oldByte = buffer.get(index);
        byte newByte = setBit(oldByte, position, value);
        buffer.put(index, newByte);
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= buffer.position()) {
            throw new IndexOutOfBoundsException("Index is out of bounds");
        }
    }

    private void ensureCapacity(int minimumCapacity) {
        if (buffer.capacity() < minimumCapacity) {
            int newCapacity = Math.max(buffer.capacity() * 2, minimumCapacity);
            ByteBuffer newBuffer = ByteBuffer.allocate(newCapacity);
            newBuffer.order(buffer.order());
            buffer.flip();
            newBuffer.put(buffer);
            buffer = newBuffer;
        }
    }

    public void fill(int length, byte value) {
        ensureCapacity(buffer.position() + length);
        byte[] fillArray = new byte[length];
        Arrays.fill(fillArray, value);
        buffer.put(fillArray);
    }

    public int getRemainingCapacity() {
        return buffer.remaining();
    }

    public byte[] toByteArray() {
        byte[] result = new byte[buffer.position()];
        buffer.flip();
        buffer.get(result);
        return result;
    }

    public void clear() {
        buffer.rewind();
    }
}
