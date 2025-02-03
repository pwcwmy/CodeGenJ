show table status

show full fields from user_info

show index from user_info

CDATA 是 "Character Data" 的缩写，它是 XML（以及 XHTML 和其他一些标记语言）中的一个特殊标记，用于指示其中包含的文本不应被解析器解析为标记。换句话说，CDATA 部分允许你在 XML 文档中包含大量纯文本数据，而无需对特殊字符（如 <, >, & 等）进行转义。

     <![CDATA[ and join_time >= str_to_date(#query.joinTimeStart), '%Y-%m-%d']]>
     <![CDATA[ and join_time < date_sub(str_to_date(#query.joinTimeStart), '%Y-%m-%d'), interval -1 day)]]>


用户可能在MyBatis的XML映射文件中使用了类似`#{query.isDeleted}`的表达式，或者在动态SQL的条件判断中使用了`<if test="query.isDeleted != null">`这样的语句。此时，MyBatis会尝试从`query`对象中获取`isDeleted`属性，但该属性不存在，因此抛出异常。

首先，我需要检查`UserInfoQuery`类是否有`isDeleted`属性。通常，Java中的布尔类型属性命名会以`is`开头，但根据JavaBean规范，对应的getter方法应该是`isDeleted()`，而属性名实际上是`deleted`。因此，可能存在命名不一致的问题。例如，类中可能有一个`deleted`属性，但getter方法是`isDeleted()`，这种情况下，MyBatis可能会认为属性名是`deleted`而不是`isDeleted`，导致访问`isDeleted`时出错。

另一种可能性是用户在类中确实没有定义`isDeleted`属性，或者属性名拼写错误。例如，可能实际属性名为`deleted`，但在XML中误写为`isDeleted`。或者是属性存在，但访问方式不正确，比如在MyBatis中直接使用属性名而不是getter方法对应的名称。

接下来，我需要考虑解决方案。首先，用户应该检查`UserInfoQuery`类，确认是否存在`isDeleted`属性，或者是否有对应的getter方法。如果属性是`deleted`，并且有`isDeleted()`方法，那么在MyBatis中应该使用`query.deleted`而不是`query.isDeleted`。这是因为MyBatis会根据JavaBean规范来解析属性名，即通过getter方法来确定属性名称，而不是直接使用字段名。

另外，如果用户确实需要在MyBatis中使用`isDeleted`作为属性名，那么应该在`UserInfoQuery`类中显式定义该属性，并提供对应的getter和setter方法。例如，添加一个`isDeleted`的布尔类型字段，并生成`isIsDeleted()`和`setIsDeleted()`方法，但这样可能不符合常规的命名习惯，容易引起混淆。

还有一种情况是，用户可能混淆了字段名和getter方法的命名。例如，字段名为`isDeleted`，而getter方法为`getIsDeleted()`，这种情况下，根据JavaBean规范，属性名会被识别为`isDeleted`，但这样的命名方式并不推荐，通常建议使用`deleted`作为字段名，并搭配`isDeleted()`方法。

此外，用户可能没有正确编译项目，导致MyBatis无法通过反射获取类的属性信息。例如，在使用Lombok生成getter方法时，如果没有正确配置，可能导致MyBatis无法识别生成的getter方法，从而认为属性不存在。这种情况下，需要检查项目的编译配置，确保Lombok注解处理器已正确启用。

最后，用户可能在动态SQL的条件判断中错误地引用了属性。例如，在`<if test="query.isDeleted != null">`中，`query`对象可能没有`isDeleted`属性，或者属性存在但值为null，此时需要确保属性存在并且命名正确。

总结来说，解决这个问题的步骤应该是：

1. 检查`UserInfoQuery`类中是否存在`isDeleted`属性，或对应的getter方法。

2. 确认属性名和getter方法的命名是否符合JavaBean规范。

3. 在MyBatis的XML文件中使用正确的属性名，根据实际的字段名或getter方法名来引用。

4. 如果使用Lombok，确保生成的getter方法正确，并且项目编译配置正确。

5. 测试修改后的代码，确保异常不再出现。

通过以上步骤，用户应该能够定位并解决`NoSuchPropertyException`的问题。



### 问题分析

错误信息 `org.apache.ibatis.ognl.NoSuchPropertyException: com.easyjava.entity.query.UserInfoQuery.isDeleted` 表明 MyBatis 在解析 `UserInfoQuery` 对象时，未找到名为 `isDeleted` 的属性。常见原因如下：

1. **属性未定义**：`UserInfoQuery` 类中确实没有 `isDeleted` 属性。
2. **命名不符合 JavaBean 规范**：存在字段但缺少 getter 方法，或 getter 方法命名不规范。
3. **Lombok 生成代码问题**：若使用 Lombok 自动生成 getter/setter，可能未正确生成方法。
4. **MyBatis 动态 SQL 引用错误**：在 XML 中错误引用了属性名（如混淆字段名与 getter 逻辑名）。

---

### 解决方案

#### 1. **检查 `UserInfoQuery` 类的属性定义
确认类中是否存在 `isDeleted` 属性或对应的布尔字段。例如：

```java
public class UserInfoQuery {
    private Boolean isDeleted; // 字段名是 isDeleted（不推荐）
    // 或
    private Boolean deleted;   // 字段名是 deleted（推荐）
    
    // 若字段名是 deleted，规范的 getter 应命名为 isDeleted()
    public Boolean getDeleted() {
        return deleted;
    }
    
    // 或者（针对布尔类型，规范允许 isDeleted()）
    public Boolean isDeleted() {
        return deleted;
    }
}
```

- **关键点**：MyBatis 通过 **getter 方法名** 解析属性名，而非直接使用字段名。
- **示例**：
    - 如果字段是 `deleted`，且 getter 为 `isDeleted()`，则 MyBatis 识别的属性名为 `deleted`。
    - 如果字段是 `isDeleted`，且 getter 为 `getIsDeleted()`，则属性名为 `isDeleted`。

---

#### 2. **修正 MyBatis 动态 SQL 中的属性引用
根据 `UserInfoQuery` 的实际属性名调整 XML 中的表达式：

- **场景 1**：字段名为 `deleted`，getter 为 `isDeleted()`
  ```xml
  <!-- 正确写法：使用属性名 deleted -->
  <if test="query.deleted != null">
      AND is_deleted = #{query.deleted}
  </if>
  ```

- **场景 2**：字段名为 `isDeleted`，getter 为 `getIsDeleted()`
  ```xml
  <!-- 正确写法：使用属性名 isDeleted -->
  <if test="query.isDeleted != null">
      AND is_deleted = #{query.isDeleted}
  </if>
  ```

---

#### 3. **规范命名建议
- **字段名**：推荐使用 `deleted`（而非 `isDeleted`），避免歧义。
- **Getter 方法**：布尔类型字段的 getter 命名为 `isXxx()`，其他类型用 `getXxx()`。
  ```java
  private Boolean deleted;
  
  public Boolean isDeleted() { // 符合规范
      return deleted;
  }
  ```

---

#### 4. **检查 Lombok 配置（如使用）
若通过 Lombok 的 `@Data` 或 `@Getter` 生成代码，确保注解生效：
```java
@Data
public class UserInfoQuery {
    private Boolean deleted; // Lombok 会自动生成 isDeleted()
}
```
- 编译后检查生成的 `isDeleted()` 方法是否存在。

---

#### 5. **验证 MyBatis 属性解析
在测试代码中打印 `UserInfoQuery` 的属性列表，确认 MyBatis 能识别到目标属性：
```java
// 获取类的属性元信息
Class<?> clazz = UserInfoQuery.class;
BeanWrapperImpl wrapper = new BeanWrapperImpl(clazz);
for (PropertyDescriptor pd : wrapper.getPropertyDescriptors()) {
    System.out.println("Property: " + pd.getName());
}
```
- 输出应包含 `deleted` 或 `isDeleted`（根据实际命名）。

---

### 最终修正示例

**Java 类**（规范命名）：
```java
public class UserInfoQuery {
    private Boolean deleted; // 字段名
    
    // 符合规范的 getter
    public Boolean isDeleted() {
        return deleted;
    }
    
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
```

**MyBatis XML**：
```xml
<select id="selectUsers" resultType="UserInfo">
    SELECT * FROM user_info
    <where>
        <if test="query.deleted != null">  <!-- 使用属性名 deleted -->
            AND is_deleted = #{query.deleted}
        </if>
    </where>
</select>
```

---

通过以上步骤，可解决因属性名不匹配导致的 `NoSuchPropertyException` 问题。确保代码中字段、getter 方法及 MyBatis 引用的属性名严格一致。

问题：com.mysql.cj.exceptions.CJException: Public Key Retrieval is not allowed
解决：&allowPublicKeyRetrieval=true



导致 `tableInfo.getFieldList().size()` 变化的原因是 **`removePrimaryKeyFieldInfoList` 直接引用了 `tableInfo` 的原始字段列表，并通过 `removeAll()` 移除了主键字段**。具体分析如下：

---

### **根本原因**
1. **引用传递而非副本**  
   `removePrimaryKeyFieldInfoList` 直接指向 `tableInfo.getFieldList()` 的原始列表：
   ```java
   List<FieldInfo> removePrimaryKeyFieldInfoList = tableInfo.getFieldList(); // 直接引用原始列表
   ```
  - 此时对 `removePrimaryKeyFieldInfoList` 的任何修改（如 `removeAll()`）都会直接影响 `tableInfo` 的字段列表。

2. **`removeAll()` 操作实际修改了原始列表**
   ```java
   removePrimaryKeyFieldInfoList.removeAll(entry.getValue()); // 直接操作原始列表
   ```
  - 如果 `entry.getValue()` 包含主键字段，这些字段会从 `tableInfo` 的字段列表中被永久移除，导致 `size()` 减少。

---

### **逻辑验证**
#### **场景模拟**
假设 `tableInfo.getFieldList()` 初始包含以下字段：
```java
Field1 (主键), Field2, Field3
```
执行代码后：
1. `removePrimaryKeyFieldInfoList` 指向原始列表。
2. 遍历 `keyIndexMap`，找到主键字段列表 `[Field1]`。
3. 调用 `removeAll([Field1])`，直接从原始列表中移除 `Field1`。
4. 最终 `tableInfo.getFieldList().size()` 从 3 变为 2。

---

### **修复方案**
#### 1. **操作副本而非原始列表**
避免直接修改原始字段列表，而是创建副本：
   ```java
   // 创建原始列表的副本
   List<FieldInfo> removePrimaryKeyFieldInfoList = new ArrayList<>(tableInfo.getFieldList());
   
   // 对副本进行操作
   for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
       if ("PRIMARY".equals(entry.getKey())) {
           removePrimaryKeyFieldInfoList.removeAll(entry.getValue());
       }
   }
   
   // 副本的 size 变化，原始列表不受影响
   ```

#### 2. **明确是否需要修改原始数据**
- 如果目的是 **临时过滤主键字段**，操作副本更安全。
- 如果目的是 **永久移除主键字段**，需确认设计是否合理（通常主键字段不应从元数据中移除）。

---

### **代码改进建议**
```java
// 创建副本，避免修改原始数据
List<FieldInfo> filteredFields = new ArrayList<>(tableInfo.getFieldList());
Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();

// 移除主键字段（操作副本）
for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
    if ("PRIMARY".equals(entry.getKey())) {
        filteredFields.removeAll(entry.getValue()); // 不影响 tableInfo 的原始列表
    }
}

// 使用 filteredFields 进行后续操作
```

---

### **总结**
- **问题本质**：直接操作了 `tableInfo` 的原始字段列表引用，导致 `size()` 变化。
- **修复核心**：通过操作副本来隔离对原始数据的修改。

进阶技术：代码的临时处理一定要用对象的副本 new ArrayList<>(tableInfo.getFieldList())



SimplePage

想知道在处理大数据量时，应该选择int、Integer、long还是Long。这个问题涉及到Java的基本数据类型和它们的包装类，以及在不同场景下的应用。我需要仔细分析这些类型的区别，并结合用户提供的代码来给出合理的建议。

首先，用户提供的代码中，`SimplePage`类中的所有分页参数都使用了Long类型，比如pageNo、pageSize、totalCount等。这可能是因为考虑到数据量大的时候，数值可能超过int的范围。但用户现在想确认这样的选择是否合理，或者是否有更好的选择。

接下来，我需要回忆Java中基本数据类型和包装类的特性。int是32位的有符号整数，范围是-2^31到2^31-1，也就是大约-21亿到21亿。而long是64位的，范围更大，从-2^63到2^63-1，这对于处理大数据量来说，显然更安全，尤其是在处理记录数可能超过21亿的情况下。Integer和Long是它们的包装类，用于需要对象而不是基本类型的场景，比如集合中的元素或者需要null值的情况。

然后，用户提到的数据量大时，选择合适的数据类型非常重要。如果使用int，当数据量超过21亿时，会导致溢出，结果不正确。例如，totalCount如果是int类型，当超过21亿时，会变成负数，这显然不可取。因此，对于可能超过这个范围的数值，必须使用long或Long。

接下来，分析用户代码中的分页参数。totalCount代表总记录数，当数据量极大时，比如超过十亿级别，使用Long是必要的。而pageNo和pageSize虽然单个值可能不会超过int的范围，但如果pageSize很大，比如每页显示一百万条数据，那么pageNo乘以pageSize后的start值可能超过int的范围，导致溢出。例如，pageNo是1000，pageSize是1,000,000，start就是999,000,000，这在int范围内是允许的，但如果pageNo继续增大，比如到3000，start就会变成3,000,000,000，这超过了int的最大值2,147,483,647，此时使用int会导致负数结果，显然错误。因此，start和end也应该使用long类型。

另外，考虑到分页参数可能作为方法的参数传递，或者在数据库查询中使用，使用包装类Long可以处理null值的情况，但通常情况下，分页参数不应为null，所以可能更倾向于使用基本类型long，以避免不必要的装箱拆箱开销，提高性能。不过，如果框架或库要求使用包装类，比如MyBatis在处理参数时可能需要对象类型，那么使用Long会更合适。

在用户提供的代码中，使用的是Long，这可能是因为某些框架需要对象类型，或者允许参数为null，但根据逻辑，pageNo和pageSize应该不能为null，因此在构造函数中进行了null检查，如果pageNo为null，则设置为0L。这种情况下，使用Long可能更灵活，但需要注意处理null值的情况，避免NPE。

此外，代码中的action方法负责计算总页数、调整pageNo的范围以及计算start和end。这里需要确保所有计算都是正确的，尤其是当totalCount非常大的时候，使用long可以避免中间计算时的溢出问题。例如，计算totalPage时，(totalCount + pageSize - 1) / pageSize的方式可能更安全，避免使用条件判断，但用户目前的写法是分情况处理余数，这在totalCount很大的情况下，可能需要用long来存储结果，防止溢出。

再考虑到数据库中的记录数可能非常大，比如社交平台或电商系统，用户数或订单数可能轻松超过21亿，因此使用Long是必要的。而如果使用int，当数据量超过21亿时，程序会出错，无法正确处理分页，这是不可接受的。

总结一下，选择数据类型时需要考虑以下几点：

1. 数值范围：是否可能超过int的最大值（约21亿）。

2. 框架要求：是否需要使用包装类。

3. 性能考量：基本类型更高效，但包装类允许null。

4. 代码一致性：保持所有相关参数类型一致，避免类型转换错误。

在用户的具体代码中，所有分页参数都使用Long，这是合理的，尤其是在处理大数据量时。但需要注意在构造函数和方法中正确处理null值，确保数值的有效性，比如在action方法中对pageNo进行范围限制，避免负数或过大的值。同时，在计算totalPage时，可以考虑使用更简洁的公式，如(totalCount + pageSize - 1) / pageSize，来避免条件判断，提高代码可读性。

另外，用户提到的“对于数据量大时，选择int，Integer，long，Long”，需要明确各个类型的适用场景。基本类型long在性能上更优，但无法表示null；包装类Long适用于需要对象或可能为null的情况。在大多数分页场景中，参数不应为null，因此如果框架允许，使用基本类型long可能更好。但如果代码中已经使用了Long，并且处理了null的情况，那么继续使用Long也是可行的。

最后，建议用户检查所有使用这些分页参数的地方，确保没有潜在的溢出风险，并进行充分的测试，尤其是当totalCount接近或超过int最大值时，验证分页计算的正确性。

在处理大数据量时，分页参数的数值范围可能超出 `int` 的容量（`int` 最大值为 2^31-1，约 21 亿）。为确保数据安全和代码健壮性，建议按以下规则选择数据类型：

---

### **1. 数据类型选择原则**
| **参数**       | **推荐类型** | **原因**                                                                                   |
|----------------|-------------|-------------------------------------------------------------------------------------------|
| **总记录数**   | `long/Long` | 数据量可能超过 `int` 范围（如超过 21 亿条记录）。                                           |
| **页码/页大小** | `long/Long` | 避免 `pageNo * pageSize` 计算时溢出（如页大小 100 万，页码 3 万时，结果超过 `int` 范围）。 |
| **起始位置**   | `long/Long` | `start = (pageNo - 1) * pageSize` 可能存在大数计算，需防止溢出。                           |

---

### **2. 针对 `SimplePage` 类的优化建议**
#### **(1) 统一使用 `long` 而非 `Long`（优先推荐）**
若分页参数 **不允许为 `null`**，建议使用基本类型 `long` 以提高性能，避免包装类的拆箱/装箱开销：
```java
public class SimplePage {
    private long pageNo;      // 基本类型 long
    private long pageSize;    // 基本类型 long
    private long totalCount;  // 基本类型 long
    private long totalPage;   // 基本类型 long
    private long start;       // 基本类型 long
    private long end;         // 基本类型 long

    public SimplePage(long pageNo, long pageSize, long totalCount) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        action();
    }
}
```

#### **(2) 必须使用 `Long` 的场景**
若需要兼容 `null`（如参数来自外部不可控输入），则保留 `Long`，但需在逻辑中处理 `null` 值：
```java
public class SimplePage {
    private Long pageNo;     // 包装类 Long
    private Long pageSize;   // 包装类 Long
    private Long totalCount; // 包装类 Long
    private Long totalPage;  // 包装类 Long
    private Long start;      // 包装类 Long
    private Long end;        // 包装类 Long

    public SimplePage(Long pageNo, Long pageSize, Long totalCount) {
        this.pageNo = pageNo != null ? pageNo : 0L; // 处理 null
        this.pageSize = pageSize != null ? pageSize : PageSizeEnum.SIZE20.getPageSize();
        this.totalCount = totalCount != null ? totalCount : 0L;
        action();
    }
}
```

---

### **3. 关键逻辑修正**
#### **(1) 修正 `action()` 方法**
原代码中 `action()` 存在逻辑问题（如对 `pageNo` 的赋值错误）：
```java
public void action() {
    // 错误：将 pageNo 设置为 pageSize（逻辑矛盾）
    // if (this.pageNo <= 0) {
    //     this.pageNo = PageSizeEnum.SIZE20.getPageSize();
    // }

    // 正确逻辑：确保 pageNo 最小为 1
    if (this.pageNo == null || this.pageNo <= 0) {
        this.pageNo = 1L;
    }

    // 计算总页数（优化公式）
    if (this.pageSize <= 0) {
        this.pageSize = PageSizeEnum.SIZE20.getPageSize();
    }
    this.totalPage = (this.totalCount + this.pageSize - 1) / this.pageSize;

    // 限制 pageNo 范围
    this.pageNo = Math.max(1, Math.min(this.pageNo, this.totalPage));

    // 计算起始位置
    this.start = (this.pageNo - 1) * this.pageSize;
    this.end = this.pageSize;
}
```

#### **(2) 公式优化**
- **总页数计算**：使用 `(totalCount + pageSize - 1) / pageSize` 替代条件判断，更简洁且高效。
- **页码范围限制**：直接通过 `Math.min` 和 `Math.max` 确保 `pageNo` 在合理范围内。

---

### **4. 完整代码示例（优化后）**
```java
public class SimplePage {
    private long pageNo;
    private long pageSize;
    private long totalCount;
    private long totalPage;
    private long start;
    private long end;

    public SimplePage(long pageNo, long pageSize, long totalCount) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        action();
    }

    private void action() {
        // 默认值处理
        if (this.pageSize <= 0) {
            this.pageSize = PageSizeEnum.SIZE20.getPageSize();
        }
        if (this.totalCount < 0) {
            this.totalCount = 0;
        }

        // 计算总页数
        this.totalPage = (this.totalCount + this.pageSize - 1) / this.pageSize;

        // 限制 pageNo 范围
        this.pageNo = Math.max(1, Math.min(this.pageNo, this.totalPage));

        // 计算起始位置
        this.start = (this.pageNo - 1) * this.pageSize;
        this.end = this.pageSize;
    }

    // Getters and Setters (略)
}
```

---

### **5. 性能与安全性分析**
| **场景**                  | **int/Integer**       | **long/Long**          |
|--------------------------|-----------------------|------------------------|
| **最大支持记录数**        | 约 21 亿（易溢出）     | 约 922 亿亿（安全）     |
| **内存占用**              | 较小（4 字节）         | 较大（8 字节）          |
| **空值处理**              | 不支持（需额外逻辑）   | 支持（`Long` 可为 `null`） |
| **计算安全性**            | 大数乘法易溢出         | 支持更大范围计算         |

---

### **总结**
- **优先使用 `long`**：适用于所有分页参数，避免溢出风险。
- **仅在需要 `null` 时使用 `Long`**：如外部参数可能未传值。
- **修正逻辑错误**：确保分页计算正确，尤其是 `totalPage` 和 `pageNo` 的范围限制。