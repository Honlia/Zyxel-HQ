package cn.superfw.framework.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.superfw.framework.exception.ClassLoadException;

/**
 * 类操作帮助类
 * @author chenchao
 * @since 1.0
 */
public class ClassUtil {
    private static final Logger log = LoggerFactory.getLogger(ClassUtil.class);


    /**
     * 生成するオブジェクトのクラス名を元にインスタンスを生成します。
     *
     * クラス名が null で渡された場合、
     *  NullPointerException がスローされます。
     * @param className
     * 生成するオブジェクトのクラス名
     * @return
     * 生成したインスタンス
     * @throws ClassLoadException
     * インスタンス生成時に発生した例外をラップした例外
     */
    public static Object create(String className) throws ClassLoadException {

        // 参照を生成
        Object object = null;

        // クラスローダを取得する
        Thread t = Thread.currentThread();
        ClassLoader cl = t.getContextClassLoader();

        try {
            // Classインスタンスを生成し、オブジェクトを生成する。
            object = cl.loadClass(className).newInstance();

        } catch (InstantiationException e) {
            // 抽象クラスだった場合
            throw new ClassLoadException(e);
        } catch (IllegalAccessException e) {
            // コンストラクタにアクセス出来なかった場合
            throw new ClassLoadException(e);
        } catch (ClassNotFoundException e) {
            // *.classファイルが見つからない場合
            throw new ClassLoadException(e);
        }

        // 生成されたオブジェクトを返す。
        return object;
    }

    /**
     * 生成するオブジェクトのクラス名を元にインスタンスを生成します。
     *
     * クラス名が null で渡された場合、
     *  NullPointerException がスローされます。
     * @param className
     * 生成するオブジェクトのクラス名
     * @param constructorParameter
     * 生成するオブジェクトのコンストラクタのパラメータ<br>
     * (注:)このパラメータは生成するオブジェクトの引数の順番と対応する必要があります。
     * @return
     * 生成したインスタンス
     * @throws ClassLoadException
     * インスタンス生成時に発生した例外をラップした例外
     */
    public static Object create(String className,
                                 Object[] constructorParameter)
                                 throws ClassLoadException {

        // 参照の生成
        @SuppressWarnings("rawtypes")
        Constructor[] constructors = null;

        // クラスローダを取得する
        Thread t = Thread.currentThread();
        ClassLoader cl = t.getContextClassLoader();

        try {
            // このClassインスタンスの持つ、
            //全てのコンストラクタオブジェクトを取得。
            constructors = cl.loadClass(className).getConstructors();
        } catch (SecurityException e) {
            // 情報へのアクセスが拒否された場合
            throw new ClassLoadException(e);
        } catch (ClassNotFoundException e) {
            // *.classファイルが見つからない場合
            throw new ClassLoadException(e);
        }

        // 任意のオブジェクトが生成されるまで、
        // 全てのコンストラクタオブジェクトからの生成を試みる
        for (int i = 0; i < constructors.length; i++) {

            // 参照を生成
            Object object = null;

            try {
                // コンストラクタに引数を渡し、オブジェクトの生成を試みる。
                object = constructors[i].newInstance(constructorParameter);
            } catch (IllegalArgumentException e) {
                // 不正な引数が渡された場合
                continue;
            } catch (InstantiationException e) {
                // 抽象クラスだった場合
                throw new ClassLoadException(e);
            } catch (IllegalAccessException e) {
                // コンストラクタにアクセス出来なかった場合
                throw new ClassLoadException(e);
            } catch (InvocationTargetException e) {
                // コンストラクタがスローする例外をラップ
                throw new ClassLoadException(e);
            }

            // オブジェクトが生成されていた場合処理を終了
            if (object != null) {
                return object;
            }

        }

        // オブジェクトが、生成できなかった場合は、例外をスローする
        throw new ClassLoadException(
            new IllegalArgumentException("class name is " + className));
    }


    /**
     * 根据指定的类名称加载类
     * @param className
     * @return
     * @throws ClassNotFoundException
     */
    public static Class<?> loadClass(String className) throws ClassNotFoundException {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException ex) {
                try {
                    return ClassLoader.class.getClassLoader().loadClass(className);
                } catch (ClassNotFoundException exc) {
                    throw exc;
                }
            }
        }
    }

    /**
     * 实例化指定的类名称（全路径）
     * @param clazzStr
     * @return
     * @throws Exception
     */
    public static Object newInstance(String clazzStr) {
        try {
            log.debug("loading class:" + clazzStr);
            Class<?> clazz = loadClass(clazzStr);
            return instantiate(clazz);
        } catch (ClassNotFoundException e) {
            log.error("Class not found.", e);
        } catch (Exception ex) {
            log.error("类型实例化失败[class=" + clazzStr + "]\n" + ex.getMessage());
        }
        return null;
    }

    /**
     * 根据类的class实例化对象
     * @param clazz
     * @return
     */
    public static <T> T instantiate(Class<T> clazz) {
        if (clazz.isInterface()) {
            log.error("所传递的class类型参数为接口，无法实例化");
            return null;
        }
        try {
            return clazz.newInstance();
        } catch (Exception ex) {
            log.error("检查传递的class类型参数是否为抽象类?", ex.getCause());
        }
        return null;
    }
}
