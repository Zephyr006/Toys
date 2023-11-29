## LayoutManager
在Swing中，有多种LayoutManager可供我们选择来管理组件的布局，他们有些是预定义的，有些则可以自定义。不同的LayoutManager选择会直接影响到组件在容器中的布局和定位。以下是一些常见的Swing LayoutManager：

- BorderLayout：将容器分为5个区域：北(NORTH)、南(SOUTH)、东(EAST)、西(WEST)和中(CENTER)。每个区域只能放置一个组件，且组件最大可能覆盖整个区域。
- FlowLayout：是一种简单的布局管理器，组件按顺序放置在它们的容器中。它不考虑组件的大小，也不保证组件在容器中的对齐。
- GridLayout：用于创建具有网格结构的布局。在添加组件时，会依次填充网格的行或列。
- CardLayout：用于管理多个组件（通常是JPanel），每次只显示一个。它允许在运行时切换显示的面板。
- GridBagLayout：是一种灵活的布局管理器，允许组件占据多个行或列的空间。可以设置组件的位置、大小、填充方式等。